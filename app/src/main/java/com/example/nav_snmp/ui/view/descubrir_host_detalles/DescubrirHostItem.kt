package com.example.nav_snmp.ui.view.descubrir_host_detalles

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Divider
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.nav_snmp.data.model.HostModel
import com.example.nav_snmp.data.model.HostModelClass
import com.example.nav_snmp.ui.theme.SnmpTheme
import com.example.nav_snmp.utils.RecursoTipoDispositivo
import com.example.nav_snmp.utils.TipoDispositivo
import java.security.cert.CertPathChecker


@Composable
fun DescubrirHostItem(
    modifier: Modifier = Modifier,
    hostModel: HostModelClass = HostModelClass(
        0,
        "Nombre del dispositivo",
        " ",
        TipoDispositivo.HOST.toString(),
        "v1",
        161,
        "public",
        false,
        null
    ),
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit,
    onClose: () -> Unit
) {

    var recursoTipoDispositivo = RecursoTipoDispositivo()

    @DrawableRes
    val imagen: Int = recursoTipoDispositivo.obtenerRecurso(hostModel.tipoDeDispositivo)

    Column(modifier = modifier.clickable() { onCheckedChange(!checked) }) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween, // Distribuir espacio entre los elementos
            verticalAlignment = Alignment.CenterVertically // Centrar verticalmente
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                Image(
                    modifier = Modifier.size(33.dp),
                    painter = painterResource(id = imagen),
                    contentDescription = "Dispositivo",
                )
                Spacer(modifier = Modifier.size(20.dp))

                Column {
                    Text(text = hostModel.nombreHost)
                    Text(text = hostModel.direccionIP)
                }
            }



            Checkbox(
                modifier = Modifier.padding(vertical = 8.dp),
                checked = checked,
                onCheckedChange = onCheckedChange
            )
        }

        HorizontalDivider()
    }

}


//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun DescubrirHostItemPreview() {
//    SnmpTheme() {
//        DescubrirHostItem(
//            Modifier.padding(horizontal = 8.dp),
//            checked =
//            )
//    }
//}

