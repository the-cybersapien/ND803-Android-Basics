package xyz.cybersapien.givemebooks;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.net.URL;
import java.util.List;

/**
 * Created by Cybersapien on 1/10/16.
 * This will be is Adapter used
 */

public class BooksAdapter extends ArrayAdapter<Book>{

    ImageView bookImageView;

    public BooksAdapter(Context context,List<Book> objects) {
        super(context,R.layout.book_item, objects);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View listView = convertView;

        //Inflate List view if listView is empty.
        if (listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.book_item, parent, false);
        }

        //Get the Current Book to set ListView Items with
        Book currentBook = getItem(position);

        //Set the book Title
        TextView bookTitleView = (TextView) listView.findViewById(R.id.book_title);
        bookTitleView.setText(currentBook.getTitle());

        //Set the book subTitle
        TextView bookSubTitleView = (TextView) listView.findViewById(R.id.book_subtitle);
        if (currentBook.getSubTitle().equals(Book.NO_SUBTITLE) || currentBook.getSubTitle().trim().equals("")){
            bookSubTitleView.setVisibility(View.GONE);
        } else {
            bookSubTitleView.setText(currentBook.getSubTitle());
            bookSubTitleView.setVisibility(View.VISIBLE);
        }

        //Set the Author's name
        TextView authorView = (TextView) listView.findViewById(R.id.book_author);
        if (currentBook.getAuthor().equals(Book.NO_AUTHOR)){
            authorView.setVisibility(View.GONE);
        } else {
            authorView.setText(currentBook.getAuthor());
            authorView.setVisibility(View.VISIBLE);
        }

        //Set the Image for the book
        bookImageView = (ImageView) listView.findViewById(R.id.book_imageView);

        if (!currentBook.getImageURL().equals(Book.NO_IMAGE)){
            Picasso.with(getContext()).load(currentBook.getImageURL()).into(bookImageView);
            bookImageView.setVisibility(View.VISIBLE);
        } else {
            bookImageView.setVisibility(View.GONE);
        }


        return listView;
    }
}
