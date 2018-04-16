package cr.ac.unadeca.galeriafrank.activities;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.flexbox.AlignItems;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayoutManager;
import com.google.android.flexbox.JustifyContent;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import cr.ac.unadeca.galeriafrank.R;
import cr.ac.unadeca.galeriafrank.database.Models.Imagen;
import cr.ac.unadeca.galeriafrank.fragments.FragmentRandomImage;
import cr.ac.unadeca.galeriafrank.fragments.FragmentRandomImageRX;
import cr.ac.unadeca.galeriafrank.subclases.RunImage;
import cr.ac.unadeca.galeriafrank.util.Adapter;
import cr.ac.unadeca.galeriafrank.util.ImageAdapter;
import cr.ac.unadeca.galeriafrank.util.Session;
import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
private static final int agregarImagen =1001;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */

    private Adapter mSectionsPagerAdapter;
    private static Context QuickContext;
    private static RecyclerView recyclerView;
    private Session sesion;
    private final static CompositeDisposable disposables = new CompositeDisposable();
    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sesion = new Session(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        QuickContext = this;
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new Adapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        TabLayout tabLayout = findViewById(R.id.tabs);
        //agregar el fragmentos
        mSectionsPagerAdapter.addFragment(new ImageFragment(), "Galeria");
        mSectionsPagerAdapter.addFragment(new FragmentRandomImage(), "Galería Random");
        mSectionsPagerAdapter.addFragment(new FragmentRandomImageRX(), "Galeria Rx");
        mViewPager.setAdapter(mSectionsPagerAdapter);
        tabLayout.setupWithViewPager(mViewPager);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nuevaImagen();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == agregarImagen & resultCode == RESULT_OK) {
            setupRecyclerView();
        }
    }

    private  void nuevaImagen(){
        Intent imagen = new Intent(this, AgregarImagenActivity.class);
        startActivityForResult(imagen, agregarImagen);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }else if(id==R.id.logout){
            final AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setPositiveButton("Si", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int i) {
                    sesion.logoutUser();
                    finish();
                }
            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    dialogInterface.dismiss();
                }
            }).setMessage("¿Seguro quiere cerrar la sesión?");
            builder.show();

        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public static class ImageFragment extends Fragment{

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            FlexboxLayoutManager layoutManager = new FlexboxLayoutManager(QuickContext);
            layoutManager.setFlexWrap(FlexWrap.WRAP);
            layoutManager.setAlignItems(AlignItems.BASELINE);
            layoutManager.setJustifyContent(JustifyContent.CENTER);


            recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView);
            recyclerView.setLayoutManager(layoutManager);
            setHasOptionsMenu(true);
            return rootView;
        }

        @Override
        public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            setupRecyclerView();
        }

        @Override
        public void onResume() {
            super.onResume();
        }

    }


    public static void setupRecyclerView(){
cargarImagenes();
    }
    private static void cargarImagenes() {
        disposables.add(sampleObservable()
                // Run on a background thread
                .subscribeOn(Schedulers.io())
                // Be notified on the main thread
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<List<RunImage>>() {
                    @Override public void onComplete() {
                        System.out.print("Se termino el proceso");
                    }

                    @Override public void onError(Throwable e) {
                        System.out.print("Ha habido un error");                    }

                    @Override public void onNext(List<RunImage> images) {
                        ImageAdapter adapter = new ImageAdapter(images, QuickContext);
                        recyclerView.setAdapter(adapter);
                    }
                }));
    }
    //todo proceso a observar
    static Observable<List<RunImage>> sampleObservable() {
        //todo esta es la sintaxis de Rx y no se cambia mucho
        return Observable.defer(new Callable<ObservableSource<? extends List<RunImage>>>() {
            @Override public ObservableSource<? extends List<RunImage>> call() throws Exception {
                // Do some long running operation
                List<RunImage> images= new ArrayList<>();
                try{
                    List<Imagen> imagenes = SQLite.select().from(Imagen.class).queryList();
                    RunImage imageR;
                    for(Imagen image : imagenes){
                        imageR = new RunImage();
                        imageR.url = image.uri;
                        imageR.author= image.nombre;
                        imageR.author= image.autor;
                        images.add(imageR);
                    }


                }catch (Exception e){
                    e.printStackTrace();
                }
                return Observable.just(images);
            }
        });
    }

}