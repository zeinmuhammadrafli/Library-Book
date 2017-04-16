package id.co.visionet.bookinventory.rest;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by jessica.susanto on 21/06/2016.
 */
public interface BookService {
    @GET("kategori/list_kategori")
    Call<GenreResponse> getListCategory();

    @GET("buku/list_buku")
    Call<ResponseBody> getListBook();

    @FormUrlEncoded
    @POST("buku/tambah_buku")
    Call<InsertBookResponse> insertBuku(@Field("isbn") String isbn, @Field("judul_buku") String judul_buku,
                                        @Field("pengarang") String pengarang_buku,
                                        @Field("tahun_buku") int tahun, @Field("sinopsis_buku") String sinopsis,
                                        @Field("kat_id") int kategori, @Field("image_buku") String cover);

    @FormUrlEncoded
    @POST("buku/update_buku")
    Call<GeneralResponse> updateBuku(@Field("id_buku") int book_id, @Field("isbn") String isbn,
                                     @Field("judul_buku") String judul_buku, @Field("pengarang") String pengarang_buku,
                                     @Field("tahun_buku") int tahun, @Field("sinopsis_buku") String sinopsis,
                                     @Field("kat_id") int kategori, @Field("image_buku") String cover);

    @FormUrlEncoded
    @POST("buku/delete_buku")
    Call<GeneralResponse> deleteBuku(@Field("id_buku") int book_id);

}
