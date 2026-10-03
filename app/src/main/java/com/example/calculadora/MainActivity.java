package com.example.calculadora;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import net.objecthunter.exp4j.Expression;
import net.objecthunter.exp4j.ExpressionBuilder;

public class MainActivity extends AppCompatActivity {

    private EditText entradaEditText;
    private TextView resultadoTextView;
    // Se eliminó la variable global 'calcularButton' para resolver la advertencia de "Field can be converted to a local variable"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        entradaEditText = findViewById(R.id.editTextNumberDecimal);
        resultadoTextView = findViewById(R.id.textView2);

        // Corrección 1: Definido como variable local
        Button calcularButton = findViewById(R.id.button16);

        // Corrección 2: Se reemplazó el OnClickListener anónimo por una expresión Lambda abreviada
        if (calcularButton != null) {
            calcularButton.setOnClickListener(v -> calcularResultado());
        }

        View decorView = findViewById(android.R.id.content);
        if (decorView != null) {
            ViewCompat.setOnApplyWindowInsetsListener(decorView, (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        }
    }

    // Corrección 3: Se añade '@SuppressLint("SetTextI18n")' para decirle a Android Studio que sabemos
    // que el texto está en español (hardcoded) y no necesitamos traducirlo en un archivo strings.xml por ahora.
    @SuppressLint("SetTextI18n")
    private void calcularResultado() {
        String textoExpresion = entradaEditText.getText().toString().trim();

        if (textoExpresion.isEmpty()) {
            Toast.makeText(this, "Por favor ingresa una operación", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            double resultado = evaluarExpresion(textoExpresion);

            if (resultadoTextView != null) {
                if (resultado % 1 == 0) {
                    resultadoTextView.setText("Resultado: " + (int) resultado);
                } else {
                    resultadoTextView.setText("Resultado: " + resultado);
                }
            }
        } catch (IllegalArgumentException e) {
            if (resultadoTextView != null) {
                resultadoTextView.setText("Error: Expresión inválida");
            }
            Toast.makeText(this, "Expresión matemática inválida", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            if (resultadoTextView != null) {
                resultadoTextView.setText("Error al calcular");
            }
            Toast.makeText(this, "Error al procesar la operación", Toast.LENGTH_SHORT).show();
        }
    }

    private double evaluarExpresion(String expresion) {
        Expression expression = new ExpressionBuilder(expresion).build();
        return expression.evaluate();
    }
}
