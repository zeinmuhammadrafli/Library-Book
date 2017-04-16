package id.co.visionet.bookinventory.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import id.co.visionet.bookinventory.R;
import id.co.visionet.bookinventory.database.Book_DAO;
import id.co.visionet.bookinventory.database.Category_DAO;
import id.co.visionet.bookinventory.helper.SpinnerObject;
import id.co.visionet.bookinventory.model.Book;
import id.co.visionet.bookinventory.rest.ApiClient;
import id.co.visionet.bookinventory.rest.BookService;
import id.co.visionet.bookinventory.rest.GeneralResponse;
import id.co.visionet.bookinventory.rest.InsertBookResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BookFormActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.editBookTitle)
    EditText editBookTitle;
    @BindView(R.id.editBookAuthor)
    EditText editBookAuthor;
    @BindView(R.id.spinnerGenre)
    Spinner spinnerGenre;
    @BindView(R.id.editIsbn)
    EditText editISBN;
    @BindView(R.id.editPublishedYear)
    EditText editPublishYear;
    @BindView(R.id.editSynopsis)
    EditText editSynopsis;
    @BindView(R.id.imgBookCover)
    ImageButton imgBookCover;
    @BindView(R.id.btnSave)
    Button btnSave;

    String ISBN = "";
    Book book;
    ProgressDialog dialog;
    BookService apiService = ApiClient.getClient().create(BookService.class);
    private Book_DAO bookDAO;
    private Category_DAO categoryDAO;
    private int PICK_IMAGE_REQUEST = 1;
    private Bitmap bitmap;
    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_form);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        spinnerGenre.setPrompt("Choose Genre");
        bookDAO = new Book_DAO(this);
        categoryDAO = new Category_DAO(this);
        categoryDAO.open();
        List<SpinnerObject> genres = categoryDAO.getGenres();
        ArrayAdapter<SpinnerObject> genreAdapter = new ArrayAdapter<SpinnerObject>(this,
                android.R.layout.simple_spinner_dropdown_item, genres);
        spinnerGenre.setAdapter(genreAdapter);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            ISBN = bundle.getString("ISBN");
            bookDAO.open();
            book = bookDAO.getBook(ISBN);
            bookDAO.close();
            editISBN.setText(book.getISBN());
            editPublishYear.setText(book.getPublished_year() + "");
            editBookAuthor.setText(book.getBook_author());
            editBookTitle.setText(book.getBook_title());
            spinnerGenre.setSelection(getCustomSpinnerIndex(genres, book.getBook_category()));
            editISBN.setEnabled(false);
            editSynopsis.setText(book.getBook_synopsis());
            if (book.getBook_cover() != null) {
                if (!book.getBook_cover().contains("http://")) {
                    byte[] data = Base64.decode(book.getBook_cover(), Base64.NO_WRAP);
                    bitmap = BitmapFactory.decodeByteArray(data,
                            0, data.length);
                    imgBookCover.setImageBitmap(bitmap);
                }
            }
            getSupportActionBar().setTitle(book.getBook_title());
        } else {
            book = new Book();
        }

        imgBookCover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileChooser();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validate()) {
                    book.setBook_title(editBookTitle.getText().toString());
                    book.setBook_author(editBookAuthor.getText().toString());
                    book.setPublished_year(Integer.parseInt(editPublishYear.getText().toString()));
                    book.setBook_category(((SpinnerObject) spinnerGenre.getSelectedItem()).getId());
                    book.setBook_synopsis(editSynopsis.getText().toString());
                    book.setBook_cover(getStringImage(bitmap));

                    boolean success = false;
                    long id = 0;
                    bookDAO.open();

                    dialog = new ProgressDialog(BookFormActivity.this);
                    dialog.setIndeterminate(true);
                    dialog.setMessage("Submit To Server...");
                    dialog.setCancelable(false);

                    if (ISBN.equals("")) {
                        book.setISBN(editISBN.getText().toString());
                        try {
                            id = bookDAO.insertBook(book);
                            success = true;
                        } catch (SQLiteException e) {
                            Toast.makeText(BookFormActivity.this, "ISBN must be unique", Toast.LENGTH_SHORT).show();
                        }
                        bookDAO.close();
                        if (success) {
                            dialog.show();
                            submitBuku(book, id);
                        }
                    } else {
                        try {
                            bookDAO.updateBook(book);
                            success = true;
                        } catch (SQLiteException e) {
                            Toast.makeText(BookFormActivity.this, "ISBN must be unique", Toast.LENGTH_SHORT).show();
                        }
                        bookDAO.close();
                        if (success) {
                            dialog.show();
                            updateBuku(book);
                        }
                    }
                }
            }
        });
    }

    // untuk mendapatkan selected index dari item by value
    public int getCustomSpinnerIndex(List<SpinnerObject> objects, int value) {
        int index = 0;

        for (int i = 0; i < objects.size(); i++) {
            if (objects.get(i).getId() == value) {
                index = i;
                break;
            }
        }
        return index;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imgBookCover.setImageBitmap(bitmap);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public String getStringImage(Bitmap bmp) {
        if (bmp != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] imageBytes = baos.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.NO_WRAP);
            return encodedImage;
        } else
            return null;
    }

    private boolean validate() {
        boolean valid = true;

        String isbn = editISBN.getText().toString();
        String booktitle = editBookTitle.getText().toString();
        String bookauthor = editBookAuthor.getText().toString();
        String publishedYear = editPublishYear.getText().toString();

        if (isbn.isEmpty()) {
            editISBN.setError("Enter ISBN");
            valid = false;
        } else {
            editISBN.setError(null);
        }

        if (booktitle.isEmpty()) {
            editBookTitle.setError("Enter Book Title");
            valid = false;
        } else {
            editBookTitle.setError(null);
        }

        if (bookauthor.isEmpty()) {
            editBookAuthor.setError("Enter Book Author");
            valid = false;
        } else {
            editBookAuthor.setError(null);
        }

        if (publishedYear.isEmpty() || publishedYear.length() < 4) {
            editPublishYear.setError("Publish Year empty or must in yyyy format");
            valid = false;
        } else {
            editPublishYear.setError(null);
        }

        return valid;
    }

    private void submitBuku(final Book book, final long id) {
        Call<InsertBookResponse> call = apiService.insertBuku(book.getISBN(), book.getBook_title(), book.getBook_author(),
                book.getPublished_year(), book.getBook_synopsis(), book.getBook_category(), book.getBook_cover());
        call.enqueue(new Callback<InsertBookResponse>() {
            @Override
            public void onResponse(Call<InsertBookResponse> call, Response<InsertBookResponse> response) {
                String message = response.body().getMessage();
                dialog.dismiss();
                if (response.body().getResult().equalsIgnoreCase("ok")) {
                    int book_id = response.body().getId_buku();
                    bookDAO.open();
                    bookDAO.updateBookId(book_id, id);
                    bookDAO.close();

                    Intent i = new Intent();
                    i.putExtra("title", book.getBook_title());
                    setResult(RESULT_OK, i);
                    finish();
                } else {
                    Toast.makeText(BookFormActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<InsertBookResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updateBuku(final Book book) {
        Call<GeneralResponse> call = apiService.updateBuku(book.getBook_id(), ISBN, book.getBook_title(),
                book.getBook_author(), book.getPublished_year(), book.getBook_synopsis(),
                book.getBook_category(), book.getBook_cover());
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                String message = response.body().getMessage();
                dialog.dismiss();
                if (response.body().getResult().equalsIgnoreCase("ok")) {
                    Intent i = new Intent();
                    i.putExtra("title", book.getBook_title());
                    setResult(RESULT_OK, i);
                    finish();
                } else
                    Toast.makeText(BookFormActivity.this, message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
