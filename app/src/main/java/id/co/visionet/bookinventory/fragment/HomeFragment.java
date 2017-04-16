package id.co.visionet.bookinventory.fragment;

import android.app.Activity;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import id.co.visionet.bookinventory.R;
import id.co.visionet.bookinventory.activity.BookFormActivity;
import id.co.visionet.bookinventory.adapter.BooksAdapter;
import id.co.visionet.bookinventory.adapter.DividerDecoration;
import id.co.visionet.bookinventory.database.Book_DAO;
import id.co.visionet.bookinventory.helper.HelperFunction;
import id.co.visionet.bookinventory.model.Book;
import id.co.visionet.bookinventory.rest.ApiClient;
import id.co.visionet.bookinventory.rest.BookService;
import id.co.visionet.bookinventory.rest.GeneralResponse;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment implements SearchView.OnQueryTextListener {
    public int TO_FORM = 1;
    public int INSERT = 2;
    public int UPDATE = 3;
    @BindView(R.id.recyclerBook)
    RecyclerView recyclerBook;
    @BindView(R.id.fab)
    FloatingActionButton btnAdd;
    ProgressDialog dialog;
    BookService apiService = ApiClient.getClient().create(BookService.class);
    private List<Book> bookList = new ArrayList<Book>();
    private BooksAdapter mAdapter;
    private Unbinder unbinder;
    private Book_DAO bookDAO;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, rootView);

        bookDAO = new Book_DAO(getActivity());
        bookDAO.open();
        bookList = bookDAO.getAllBooks();

        mAdapter = new BooksAdapter(getActivity(), bookList);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerBook.setLayoutManager(mLayoutManager);
        recyclerBook.setItemAnimator(new DefaultItemAnimator());
        recyclerBook.addItemDecoration(new DividerDecoration(getActivity()));

        recyclerBook.setAdapter(mAdapter);
        recyclerBook.addOnItemTouchListener(new HelperFunction.RecyclerTouchListener(getActivity(), recyclerBook,
                new HelperFunction.ClickListener() {
                    @Override
                    public void onClick(View view, int position) {
                        Intent i = new Intent(getActivity(), BookFormActivity.class);
                        i.putExtra("ISBN", bookList.get(position).getISBN());
                        startActivityForResult(i, UPDATE);
                    }

                    @Override
                    public void onLongClick(View view, final int position) {
                        final Book book = bookList.get(position);
                        AlertDialog dialog = new AlertDialog.Builder(getActivity()).setTitle("Delete")
                                .setMessage("Are you sure to delete " + book.getBook_title() + " ?").setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // TODO Auto-generated method stub
                                        deleteBuku(book.getBook_id(), book.getISBN());
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                })
                                .create();
                        dialog.show();
                    }
                }));

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BookFormActivity.class);
                startActivityForResult(i, INSERT);
            }
        });
        bookDAO.close();

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            refreshView();
            if (requestCode == INSERT) {
                Toast.makeText(getActivity(), "Book " + data.getExtras().getString("title") + " successfully added", Toast.LENGTH_SHORT).show();
            } else if (requestCode == UPDATE) {
                Toast.makeText(getActivity(), "Book " + data.getExtras().getString("title") + " successfully updated", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
                SearchView searchView = (SearchView) item.getActionView();

                searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
                searchView.setOnQueryTextListener(this);
            case R.id.action_refresh:
                dialog = new ProgressDialog(getActivity());
                dialog.setIndeterminate(true);
                dialog.setMessage("Refresh Data...");
                dialog.setCancelable(false);
                dialog.show();

                // memanggil API
                getBooks();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        mAdapter.getFilter().filter(newText);
        return true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

    public void refreshView() {
        bookList.clear();
        bookDAO.open();
        bookList.addAll(bookDAO.getAllBooks());
        bookDAO.close();
        mAdapter.notifyDataSetChanged();
    }

    public void getBooks() {
        Call<ResponseBody> call = apiService.getListBook();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                String res = "";
                try {
                    //response body mengembalikan JSON string mentah yang bisa diolah manual
                    res = response.body().string();
                } catch (java.io.IOException ex) {
                    ex.printStackTrace();
                }

                Book_DAO bookDAO = new Book_DAO(getActivity());
                try {
                    Book book = null;
                    //pengolahan hasil json manual
                    JSONObject jsonObject = new JSONObject(res);
                    if (jsonObject.getString("Result").toLowerCase().equalsIgnoreCase("ok")) {
                        JSONArray array = jsonObject.getJSONArray("Buku");

                        for (int i = 0; i < array.length(); i++) {
                            bookDAO.open();
                            book = bookDAO.getBook(array.getJSONObject(i).getString("isbn"));
                            if (book == null) {
                                book = new Book();
                                book.setISBN(array.getJSONObject(i).getString("isbn"));
                            }
                            book.setBook_id(array.getJSONObject(i).getInt("id_buku"));
                            book.setBook_title(array.getJSONObject(i).getString("judul_buku"));
                            book.setBook_author(array.getJSONObject(i).getString("pengarang"));
                            book.setPublished_year(array.getJSONObject(i).getInt("tahun_buku"));
                            book.setBook_category(array.getJSONObject(i).getInt("kat_id"));
                            book.setBook_synopsis(array.getJSONObject(i).getString("sinopsis_buku"));
                            book.setBook_cover(array.getJSONObject(i).getString("image_buku"));

                            if (book.getIdx() < 1) {
                                bookDAO.insertBook(book);
                            } else {
                                bookDAO.updateBook(book);
                            }
                            bookDAO.close();
                        }

                        refreshView();

                        dialog.dismiss();
                    }
                } catch (JSONException ex) {
                    ex.printStackTrace();
                } catch (SQLiteException ex) {
                    ex.printStackTrace();
                } finally {
                    if (bookDAO != null) {
                        bookDAO.close();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    public void deleteBuku(int bookId, final String ISBN) {

        Call<GeneralResponse> call = apiService.deleteBuku(bookId);
        call.enqueue(new Callback<GeneralResponse>() {
            @Override
            public void onResponse(Call<GeneralResponse> call, Response<GeneralResponse> response) {
                String message = response.body().getMessage();
                if (response.body().getResult().equalsIgnoreCase("ok")) {
                    bookDAO.open();
                    bookDAO.deleteBook(ISBN);
                    bookDAO.close();
                }
                refreshView();
                Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(Call<GeneralResponse> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

}
