package com.example.swapup.ui.screens

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.FocusInteraction
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.swapup.R
import com.example.swapup.data.model.Language
import com.example.swapup.navigation.Routes
import com.example.swapup.ui.theme.DarkBlue
import com.example.swapup.ui.theme.SkyBlue
import com.example.swapup.viewmodel.CreateOfferViewModel

@Composable
fun CreateOffer(
    navController: NavHostController,
    vm: CreateOfferViewModel
) {

    val context = LocalContext.current
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()) { uri->
        if(uri == null) return@rememberLauncherForActivityResult
        vm.updatePhoto(uri)
    }
    LaunchedEffect(vm.photoError) {
        if(vm.photoError != null){
            Toast.makeText(context, vm.photoError, Toast.LENGTH_SHORT).show()
        }
    }
    Column(
        modifier = Modifier.fillMaxSize().windowInsetsPadding(WindowInsets.systemBars),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(Modifier.height(8.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            IconButton(
                onClick = {
                    vm.clearAllFields()
                    navController.popBackStack()
                }
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    tint = DarkBlue,
                    modifier = Modifier.size(24.dp)
                )
            }
            Text(
                text = stringResource(R.string.create_offer),
                fontSize = 20.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue)
            Spacer(Modifier.width(32.dp))
        }
        Spacer(Modifier.height(24.dp))
        Column(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(R.string.book_title),
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue,
            )
            ValidatedTextField(
                value = vm.title,
                onValueChange = vm::updateTitle,
                onValidate = {vm.validateTitle()},
                error = vm.titleError,
                placeholder = stringResource(R.string.book_title_placeholder),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(R.string.book_author),
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue,
            )
            ValidatedTextField(
                value = vm.author,
                onValueChange = vm::updateAuthor,
                onValidate = {vm.validateAuthor()},
                error = vm.authorError,
                placeholder = stringResource(R.string.book_author_placeholder),
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(Modifier.height(8.dp))
        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = stringResource(R.string.language),
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue,
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Button(
                    onClick = {vm.updateLanguage(Language.Uzbek)},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(vm.language==Language.Uzbek) DarkBlue else Color.White,
                        contentColor = if(vm.language==Language.Uzbek) Color.White else DarkBlue,
                    ),
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, DarkBlue)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Start
                        ) {
                            Image(
                                painter = painterResource(R.drawable.uzb),
                                contentDescription = "uzb",
                                modifier = Modifier.size(32.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = stringResource(R.string.uzbek).take(3),
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                fontSize = 10.sp,
                                fontWeight = FontWeight.W500,
                            )
                        }

                    }
                }
                Button(
                    onClick = {vm.updateLanguage(Language.English)},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(vm.language==Language.English) DarkBlue else Color.White,
                        contentColor = if(vm.language==Language.English) Color.White else DarkBlue,
                    ),
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, DarkBlue)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.eng),
                                contentDescription = "eng",
                                modifier = Modifier.size(32.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = stringResource(R.string.english).take(3),
                                fontSize = 10.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                fontWeight = FontWeight.W500,
                            )
                        }

                    }
                }
                Button(
                    onClick = {vm.updateLanguage(Language.Russian)},
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if(vm.language==Language.Russian) DarkBlue else Color.White,
                        contentColor = if(vm.language==Language.Russian) Color.White else DarkBlue,
                    ),
                    modifier = Modifier.weight(1f),
                    border = BorderStroke(1.dp, DarkBlue)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Image(
                                painter = painterResource(R.drawable.ru),
                                contentDescription = "ru",
                                modifier = Modifier.size(32.dp),
                                contentScale = ContentScale.Crop
                            )
                            Spacer(Modifier.width(12.dp))
                            Text(
                                text = stringResource(R.string.russian).take(3),
                                fontSize = 10.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Clip,
                                fontWeight = FontWeight.W500,
                            )
                        }
                    }
                }
            }
        }
        Spacer(Modifier.height(16.dp))
        Column(
            modifier = Modifier.padding(horizontal = 16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.spacedBy(4.dp),
        ) {
            Text(
                text = stringResource(R.string.book_description),
                fontSize = 16.sp,
                fontWeight = FontWeight.W600,
                color = DarkBlue,
            )
            ValidatedTextField(
                value = vm.description,
                onValueChange = vm::updateDescription,
                onValidate = {vm.validateDescription()},
                error = vm.descriptionError,
                imeAction = ImeAction.None,
                placeholder = stringResource(R.string.leave_opinion),
                modifier = Modifier.fillMaxWidth().height(128.dp),
                singleLine = false,
                maxLines = 5,
            )
        }
        Spacer(Modifier.height(8.dp))
        Button(
            onClick = {
                launcher.launch("image/*")
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SkyBlue,
                contentColor = DarkBlue,
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            if(vm.photoUri == null){
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(stringResource(R.string.upload_image),fontWeight = FontWeight.W500, fontSize = 16.sp)
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "",)
                }
            } else{
                Row(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Text(stringResource(R.string.image_saved),fontWeight = FontWeight.W500, fontSize = 16.sp)
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "",)
                }
            }
        }
        Spacer(Modifier.height(32.dp))
        Button(
            onClick = {
                vm.validateTitle()
                vm.validateAuthor()
                vm.validateDescription()
                vm.validatePhoto()
                if(vm.isEverythingOk()){
                    vm.createOffer()
                    vm.clearAllFields()
                    navController.navigate(Routes.Offer.name)
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = SkyBlue,
                contentColor = DarkBlue,
            ),
            shape = RoundedCornerShape(8.dp)
        ) {
            Text(
                modifier = Modifier.padding(vertical = 8.dp),
                text = "Save",
                fontWeight = FontWeight.W500,
                fontSize = 15.sp,)
        }
    }
}

@Composable
fun ValidatedTextField(
    value: String,
    onValueChange: (String) -> Unit,
    error: String?,
    placeholder: String,
    onValidate: () -> Unit,
    modifier: Modifier,
    singleLine: Boolean = true,
    maxLines: Int = 1,
    imeAction: ImeAction = ImeAction.Done,
    onImeAction: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val focusManager = LocalFocusManager.current

    // Focus tracking
    LaunchedEffect(interactionSource) {
        interactionSource.interactions.collect { interaction ->
            if (interaction is FocusInteraction.Unfocus) {
                onValidate()
            }
        }
    }

    TextField(
        modifier = modifier,
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder) },
        isError = error != null,
        supportingText = {
            if (error != null) {
                Text(text = error, color = MaterialTheme.colorScheme.error)
            }
        },
        interactionSource = interactionSource,
        keyboardOptions = KeyboardOptions(imeAction = imeAction),
        keyboardActions = KeyboardActions(
            onAny = {
                onValidate()
                onImeAction()
                if (imeAction == ImeAction.Done) {
                    focusManager.clearFocus()
                }
            }
        ),
        shape = RoundedCornerShape(12.dp),
        colors = OutlinedTextFieldDefaults.colors(
            unfocusedBorderColor = Color.Gray,
            unfocusedPlaceholderColor = Color.Gray,
            focusedBorderColor = DarkBlue
        ),
        singleLine = singleLine,
        maxLines = maxLines,
    )
}

