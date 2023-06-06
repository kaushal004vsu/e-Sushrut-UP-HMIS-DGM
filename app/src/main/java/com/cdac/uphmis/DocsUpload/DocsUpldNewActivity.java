package com.cdac.uphmis.DocsUpload;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cdac.uphmis.R;
import com.cdac.uphmis.DocsUpload.DocsUpldGridAdapter;
import com.cdac.uphmis.DocsUpload.DocsUpldDetails;

import java.util.ArrayList;
import java.util.Objects;

public class DocsUpldNewActivity extends AppCompatActivity {
    ActivityResultLauncher<Intent> selectImagesActivityResult;
    private ArrayList<DocsUpldDetails> documentDetailsArrayList;
    private DocsUpldGridAdapter adapter;

    String url="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docs_upld_new);
        Intent intent =getIntent();
        if (intent!=null)
        {
            if (intent.getStringExtra("title").equalsIgnoreCase(getString(R.string.UD_title)))
            {

            }
        }
        Toolbar toolbar = findViewById(R.id.patient_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        documentDetailsArrayList = new ArrayList<>();
        GridView gridView = findViewById(R.id.grid_view);
        adapter = new DocsUpldGridAdapter(DocsUpldNewActivity.this, documentDetailsArrayList);
        gridView.setAdapter(adapter);
        selectImagesActivityResult = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Intent data = result.getData();
                        if (null != data) {
                            if (null != data.getClipData()) {
                                for (int i = 0; i < data.getClipData().getItemCount(); i++) {
                                    Uri uri = data.getClipData().getItemAt(i).getUri();
                                    String mimeType = getMimeType(uri);
                                    documentDetailsArrayList.add(new DocsUpldDetails(uri, mimeType));
                                }
                            } else {
                                Uri uri = data.getData();
                                String mimeType = getMimeType(uri);
                                documentDetailsArrayList.add(new DocsUpldDetails(uri, mimeType));
                            }
                            adapter.notifyDataSetChanged();

                        }
                    }
                });
    }

    public void btnSelectFiles(View view) {
        if (documentDetailsArrayList.size() > 2) {
            Toast.makeText(this, "Maximum of 3 documents can be uploaded.", Toast.LENGTH_SHORT).show();
        } else {
            getFileChooserIntent();
        }
    }

    public String getMimeType(Uri uri) {
        String mimeType;
        if (ContentResolver.SCHEME_CONTENT.equals(uri.getScheme())) {
            ContentResolver cr = getContentResolver();
            mimeType = cr.getType(uri);
        } else {
            String fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri
                    .toString());
            mimeType = MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                    fileExtension.toLowerCase());
        }
        return mimeType;
    }

    private void getFileChooserIntent() {
        String[] mimeTypes = {"image/*", "application/pdf"};
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        intent.setType("*/*");
        intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes);

        selectImagesActivityResult.launch(intent);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

