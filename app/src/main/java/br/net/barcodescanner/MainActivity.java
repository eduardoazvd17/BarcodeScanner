package br.net.barcodescanner;

import android.content.Intent;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private Button btn;
    private TextView id;
    private TextView nome;
    private TextView preco;
    private TextView descricao;
    private ImageView img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        id = findViewById(R.id.id);
        nome = findViewById(R.id.nome);
        preco = findViewById(R.id.preco);
        descricao = findViewById(R.id.descricao);
        img = findViewById(R.id.img);
        btn = findViewById(R.id.scanear);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                IntentIntegrator scanIntegrator = new IntentIntegrator(MainActivity.this);
                scanIntegrator.setPrompt("Alinhe o código de barras com a linha para ser lido");
                scanIntegrator.setBeepEnabled(true);
                scanIntegrator.setOrientationLocked(true);
                scanIntegrator.setBarcodeImageEnabled(true);
                scanIntegrator.initiateScan();
            }
        });
    }

    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        IntentResult scanningResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, intent);
        if (null == scanningResult) {
            Toast.makeText(this, "Não foi possivel obter resultados", Toast.LENGTH_LONG).show();
        } else {
            try {
                buscarProduto(scanningResult.getContents());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void buscarProduto(String codigo) throws Exception {
        final String token = "T3r_dO417gVvlaJnSB6Gug";
        final String endpoint = "https://api.cosmos.bluesoft.com.br/";
        URL url = new URL(endpoint + "gtins/" + codigo);
        HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
        conexao.setRequestMethod("GET");
        conexao.setRequestProperty("X-Cosmos-Token", token);
        conexao.connect();
        BufferedReader br = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
        String jsonString = br.readLine();
        JSONObject jsonObject = new JSONObject(jsonString);

        Picasso.get().load(jsonObject.getString("thumbnail")).into(img);
        id.setText("Id: " + jsonObject.getString("gtin"));
        nome.setText("Nome: " + jsonObject.getString("description"));
        preco.setText("Preço: " + jsonObject.getString("price"));
        JSONObject ncm = new JSONObject(jsonObject.getString("ncm"));
        descricao.setText("Descrição: " + ncm.getString("full_description"));

        conexao.disconnect();
    }
}
