package com.example.swapup.ui.screens

import android.content.ClipData
import android.content.ClipDescription
import android.os.Build
import android.os.PersistableBundle
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.SelectionContainer
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ClipEntry
import androidx.compose.ui.platform.ClipboardManager
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.swapup.R
import com.example.swapup.data.model.Demand
import com.example.swapup.navigation.Routes
import com.example.swapup.ui.theme.DarkBlue
import com.example.swapup.ui.theme.SkyBlue
import com.example.swapup.viewmodel.DemandInfoViewModel
import com.example.swapup.viewmodel.state.UiState

@Composable
fun DemandInfo(
    navController: NavHostController,
    vm: DemandInfoViewModel
) {
    val uiState = vm.uiState
    val demand = vm.demand.observeAsState(Demand()).value
    val message = vm.message
    val languageBox = vm.languageBox
    val context = LocalContext.current
    val focusManager = LocalFocusManager.current
    val clipboardManager = LocalClipboardManager.current

    LaunchedEffect(message) {
        if(message!=null){
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    LaunchedEffect(uiState) {
        if(uiState is UiState.Error){
            val message = (uiState as UiState.Error).msg
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    when(uiState){
        is UiState.Idle->{
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .windowInsetsPadding(WindowInsets.systemBars)
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,

                ) {
                item {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        IconButton(
                            onClick = {
                                navController.popBackStack()
                            }
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "",
                                modifier = Modifier.size(24.dp),
                                tint = DarkBlue
                            )
                        }
                        Spacer(Modifier.width(12.dp))
                    }
                }
                item {
                    BitmapImageLoader(
                        photoUri = demand.photo,
                        modifier = Modifier
                            .size(180.dp, height = 240.dp)
                            .clip(RoundedCornerShape(8.dp)),
                        contentScale = ContentScale.Crop)

                    Spacer(Modifier.height(16.dp))
                }

                item {
                    Text(
                        text = demand.title,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = DarkBlue
                    )

                    Text(
                        text = demand.author,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Medium,
                        color = SkyBlue
                    )

                    Spacer(Modifier.height(8.dp))
                }



                item{
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        languageBox?.let {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.language),
                                    color = DarkBlue,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W500
                                )
                                Spacer(Modifier.width(12.dp))
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White
                                    ),
                                    border = BorderStroke(1.dp, DarkBlue)
                                ){
                                    Row(
                                        modifier = Modifier.padding(6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Image(
                                            painter = painterResource(languageBox.image),
                                            contentDescription = "icon",
                                            modifier = Modifier.size(20.dp),
                                            contentScale = ContentScale.Crop
                                        )
                                        Spacer(Modifier.width(12.dp))
                                        Text(
                                            text = stringResource(languageBox.name),
                                            fontSize = 14.sp,
                                            fontWeight = FontWeight.W500,
                                        )
                                    }

                                }
                            }
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Start,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = stringResource(R.string.status),
                                color = DarkBlue,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.W500
                            )
                            Spacer(Modifier.width(12.dp))
                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(
                                    containerColor = Color.White,
                                    contentColor = SkyBlue
                                ),
                            ){
                                Row(
                                    modifier = Modifier.padding(6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Text(
                                        text = if(demand.active) stringResource(R.string.active) else stringResource(
                                            R.string.inactive),
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.W500,
                                    )
                                }

                            }
                        }
                        if(vm.isNotOwner()){
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.Start,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = stringResource(R.string.owner),
                                    color = DarkBlue,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.W500
                                )
                                Spacer(Modifier.width(12.dp))
                                Card(
                                    shape = RoundedCornerShape(12.dp),
                                    colors = CardDefaults.cardColors(
                                        containerColor = Color.White,
                                        contentColor = SkyBlue
                                    ),
                                ){
                                    Row(
                                        modifier = Modifier.padding(6.dp),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Start
                                    ) {
                                        Text(
                                            text = demand.owner,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.W500,
                                        )
                                    }

                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))
                }

                item {
                    //description
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.Start,
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            text = stringResource(R.string.book_description),
                            fontWeight = FontWeight.W500,
                            fontSize = 18.sp,
                            color = DarkBlue,
                        )
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Justify,
                            maxLines = 5,
                            overflow = TextOverflow.Ellipsis,
                            text = demand.description,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Normal,
                            color = Color.Black
                        )
                    }
                    Spacer(Modifier.height(24.dp))
                }

                item {
                    if(vm.isNotOwner() && (vm.demand.value?.active == true) ){
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            //secret code input section
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                OutlinedTextField(
                                    value = vm.secretCode,
                                    modifier = Modifier.fillMaxWidth(0.6f),
                                    onValueChange = vm::changeSecretCode,
                                    placeholder = { Text(stringResource(R.string.secret_code)) },
                                    shape = RoundedCornerShape(8.dp),
                                    isError = vm.secretCodeError != null,
                                    supportingText = {
                                        if(vm.secretCodeError != null){
                                            Text(vm.secretCodeError!!)
                                        }
                                    },
                                    colors = OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color.Gray,
                                        unfocusedPlaceholderColor = Color.Gray,
                                        focusedBorderColor = DarkBlue
                                    ),
                                    keyboardOptions = KeyboardOptions(
                                        imeAction = ImeAction.Done
                                    ),
                                    keyboardActions = KeyboardActions(
                                        onDone = {

                                            focusManager.clearFocus()
                                            vm.checkSecretCode()
                                        }
                                    )
                                )
                                Button(
                                    onClick = {
                                        focusManager.clearFocus()
                                        vm.checkSecretCode()
                                    },
                                    modifier = Modifier.padding(bottom = 16.dp),
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = SkyBlue,
                                        contentColor = DarkBlue
                                    ),
                                ) {
                                    Text(
                                        text = stringResource(R.string.submit),
                                        fontWeight = FontWeight.W500,
                                        fontSize = 16.sp,
                                    )
                                }
                            }
                            //contact with owner btn
                            Button(
                                onClick = {
                                    vm.launchTelegram(context)
                                },
                                shape = RoundedCornerShape(8.dp),
                                modifier = Modifier.fillMaxWidth(),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = SkyBlue,
                                    contentColor = DarkBlue
                                ),
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 6.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Center
                                ) {
                                    Text(
                                        text = stringResource(R.string.contact_owner),
                                        fontWeight = FontWeight.W500,
                                        fontSize = 16.sp,
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Filled.Send,
                                        contentDescription = "",
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            }
                        }
                    }
                    //offer's code section
                    if (vm.isOwner()) {
                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.code),
                                fontWeight = FontWeight.W500,
                                fontSize = 18.sp,
                                color = DarkBlue,
                            )
                            DemandCodeRow(demand = demand, vm = vm, clipboardManager = clipboardManager)
                        }
                    }
                }


            }
        }
        is UiState.Loading->{
            LoadingScreen()
        }
        is UiState.Success->{
            navController.navigate(Routes.Demand.name)
        }
        else->{}
    }
}
@Composable
fun DemandCodeRow(
    demand: Demand,
    vm: DemandInfoViewModel,
    clipboardManager: ClipboardManager
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        SelectionContainer {
            Text(
                text = demand.uid,
                fontWeight = FontWeight.W400,
                color = Color.Gray,
                fontSize = 16.sp,
                textAlign = TextAlign.Start
            )
        }
        Spacer(Modifier.width(16.dp))
        Button(
            onClick = {
                val clipData = ClipData.newPlainText("offer id", demand.uid).apply {
                    description.extras = PersistableBundle().apply {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            putBoolean(ClipDescription.EXTRA_IS_SENSITIVE, true)
                        } else {
                            putBoolean("android.content.extra.IS_SENSITIVE", true)
                        }
                    }
                }
                val clipEntry = ClipEntry(clipData)
                clipboardManager.setClip(clipEntry)
                vm.updateMessage("Offer code is saved!")
            },
            colors = ButtonDefaults.buttonColors(
                containerColor = SkyBlue,
                contentColor = DarkBlue
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    "Copy",
                    fontWeight = FontWeight.W600,
                    fontSize = 16.sp,
                    color = DarkBlue
                )
                Spacer(Modifier.width(8.dp))
                Icon(
                    imageVector = Icons.Default.Share,
                    contentDescription = "Copy Offer Code",
                    tint = DarkBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}