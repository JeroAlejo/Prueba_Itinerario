package ec.edu.epn.prueba

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ec.edu.epn.prueba.ui.theme.PruebaTheme

class MainActivity : ComponentActivity() {

    private lateinit var databaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        databaseHelper = DatabaseHelper(this)
        insertarDatosIniciales()

        enableEdgeToEdge()
        setContent {
            ListaDeEventos(
                databaseHelper = databaseHelper,
                onDetalleEventActivity = {
                    //val intent = Intent(this, DetalleEventActivity::class.java)
                    //startActivity(intent)
                }
            )
        }
    }

    private fun insertarDatosIniciales() {
        // Verifica si ya hay datos en la base de datos para evitar duplicados
        val cursor = databaseHelper.getAllPuntosInteres()
        if (cursor.count == 0) {
            databaseHelper.insertPuntoInteres("Trail en el Mirador de la Perdiz", "Mirador de la Perdiz", "31/10/2024", 30, "R.drawable.montana_1")
            databaseHelper.insertPuntoInteres("Consumo de la Mejor colada morada", "Estadio de San José", "31/10/2024", 100, "R.drawable.colada_2")
            databaseHelper.insertPuntoInteres("Trail en el Mirador de la Perdiz", "Cementerio de San José", "02/11/2024", 30, "R.drawable.cruz_3")
            databaseHelper.insertPuntoInteres("Trail en el Mirador de la Perdiz", "Estadio de San José", "03/11/2024", 100, "R.drawable.guagua_4")
        }
    }
}



@Composable
fun ListaDeEventos(databaseHelper: DatabaseHelper? = null,onDetalleEventActivity: () -> Unit){

    val eventos = listOf(
        Pair(R.drawable.montana_1,"Actividad Criminal"),
        Pair(R.drawable.colada_2,"Tráfico Vehicular"),
        Pair(R.drawable.cruz_3,"Cierres Peatonales"),
        Pair(R.drawable.guagua_4,"Desastres Naturales")

    )

    data class Lugar(
        val nombre_evento: String,
        val lugar: String,
        val fecha: String,
        val asistentes: Int, // Ejemplo: "Lat, Long"
        val foto: String       // Horarios de operación o información adicional
    )


    val lugares = mutableListOf<Lugar>()
    // Leer los datos de SQLite
    val cursor = databaseHelper?.getAllPuntosInteres()
    cursor?.use {
        while (it.moveToNext()) {
            val nombre_eventoIndex = it.getColumnIndex(DatabaseHelper.COLUMN_NAME)
            val lugarIndex =
                it.getColumnIndex(DatabaseHelper.COLUMN_PLACE)
            val fechaIndex =
                it.getColumnIndex(DatabaseHelper.COLUMN_DATE)
            val asistentesIndex =
                it.getColumnIndex(DatabaseHelper.COLUMN_NUMBER)
            val photoIndex =
                it.getColumnIndex(DatabaseHelper.COLUMN_PHOTO)


            if (nombre_eventoIndex != -1 && lugarIndex != -1 && fechaIndex != -1  && asistentesIndex != -1  && photoIndex != -1) {
                val nombre_evento = it.getString(nombre_eventoIndex)
                val lugar = it.getString(lugarIndex)
                val fecha = it.getString(fechaIndex)
                val asistentes = it.getInt(asistentesIndex)
                val foto = it.getString(photoIndex)
                lugares.add(Lugar(nombre_evento, lugar, fecha, asistentes , foto))
            } else {
                Log.e("DatabaseError", "Una o más columnas no fueron encontradas en el cursor.")
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF1E0F1E)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(10.dp))

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 20.dp)
                .clip(RoundedCornerShape(50.dp))
                .background(Color.White),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(40.dp))
            Text(
                text = "Eventos en San Jose por el feriado de Noviembre",
                color = Color.Black,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .padding(top = 10.dp)
            )

            LazyVerticalGrid(
                columns = GridCells.Fixed(1),
                contentPadding = PaddingValues(8.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(lugares.size) { index ->
                    val (nombre_evento, lugar, fecha, asistentes, foto) = lugares[index]

                    // Asignar una imagen manualmente según el índice
                    val imagenResId = when (index) {
                        0 -> R.drawable.montana_1
                        1 -> R.drawable.colada_2
                        2 -> R.drawable.cruz_3
                        3 -> R.drawable.guagua_4
                        else -> R.drawable.montana_1 // Imagen por defecto
                    }

                    Card(
                        modifier = Modifier
                            .padding(10.dp)
                            .fillMaxWidth()
                            .shadow(elevation = 4.dp, shape = RoundedCornerShape(12.dp)),
                        shape = RoundedCornerShape(12.dp),


                    ) {
                        Row(
                            modifier = Modifier
                                .padding(12.dp)
                                .fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Imagen con margen circular
                            Image(
                                painter = painterResource(id = imagenResId),
                                contentDescription = "Imagen del evento",
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(CircleShape)
                                    .border(2.dp, Color.Gray, CircleShape),
                                contentScale = ContentScale.Crop
                            )

                            Spacer(modifier = Modifier.width(16.dp))

                            // Información del evento
                            Column(
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                // Nombre del evento
                                Text(
                                    text = nombre_evento,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                // Lugar
                                Text(
                                    text = lugar,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Light,
                                    color = Color.Gray,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                // Fecha
                                Text(
                                    text = fecha,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Normal,
                                    color = Color.Black,
                                    modifier = Modifier.padding(bottom = 4.dp)
                                )
                                // Asistentes
                                Text(
                                    text = "Asistentes: $asistentes",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = Color(0xFF800080)
                                )
                            }
                        }
                    }
                }
            }

        }
        }
    }


@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {

    ListaDeEventos(onDetalleEventActivity = {})
}