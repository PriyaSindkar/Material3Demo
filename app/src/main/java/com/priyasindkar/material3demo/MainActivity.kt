package com.priyasindkar.material3demo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetDefaults
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.DatePickerFormatter
import androidx.compose.material3.ElevatedButton
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.SheetState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.priyasindkar.material3demo.ui.theme.Material3DemoTheme
import com.priyasindkar.material3demo.ui.theme.Purple40
import com.priyasindkar.material3demo.ui.theme.Purple40Alpha30
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.util.Date
import java.util.Locale

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Material3DemoTheme(dynamicColor = false) {
                Surface(
                    modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background
                ) {
                    Material3Components()
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Material3Components() {
    val scope = rememberCoroutineScope()
    val sheetState = SheetState(skipPartiallyExpanded = false, skipHiddenState = false)
    val scaffoldState = rememberBottomSheetScaffoldState(bottomSheetState = sheetState)

    var openModalBottomSheet by rememberSaveable { mutableStateOf(false) }
    var skipPartiallyExpanded by remember { mutableStateOf(false) }
    val bottomSheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = skipPartiallyExpanded
    )

    var openDatePickerDialog by remember { mutableStateOf(false) }

    BottomSheetScaffold(modifier = Modifier.fillMaxHeight(),
        scaffoldState = scaffoldState,
        sheetPeekHeight = 128.dp,
        sheetShadowElevation = 32.dp,
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(text = "Material 3 Demo") },
                colors = TopAppBarDefaults.mediumTopAppBarColors(
                    containerColor = Purple40,
                    titleContentColor = Color.White
                )
            )
        },
        sheetDragHandle = {
            BottomSheetDefaults.DragHandle(
                color = Color.Red,
                shape = RoundedCornerShape(2.dp),
                width = 48.dp,
                height = 8.dp
            )
        },
        sheetContent = {
            BottomSheetContent {
                scope.launch {
                    scaffoldState.bottomSheetState.let {
                        if (it.isVisible) it.hide() else it.show()
                    }
                }
            }
        },
        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(state = ScrollState(0), enabled = true),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                ElevatedButton(
                    modifier = Modifier
                        .wrapContentSize(),
                    onClick = {
                        scope.launch {
                            scaffoldState.bottomSheetState.let {
                                if (it.isVisible) it.hide() else it.show()
                            }
                        }
                    }
                ) {
                    Text(text = "Standard Bottom Sheet")
                }
                VerticalSpace()
                ElevatedButton(onClick = { openModalBottomSheet = !openModalBottomSheet }) {
                    Text(text = "Show Modal Bottom Sheet")
                }
                VerticalSpace()
                ElevatedButton(
                    modifier = Modifier.wrapContentSize(),
                    onClick = { openDatePickerDialog = true }
                ) {
                    Text(text = "DatePickerDialog")
                }
                DatePickerComponent()

            }

            // Modal Sheet content
            if (openModalBottomSheet) {
                ModalBottomSheetComponent(bottomSheetState, scope) {
                    openModalBottomSheet = false
                }
            }

            // Date picker dialog content
            if (openDatePickerDialog) {
                DatePickerDialogComponent {
                    openDatePickerDialog =  false
                }
            }
        })
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DatePickerDialogComponent(
    hideDatePickerDialog: () -> Unit
) {
    val datePickerState = rememberDatePickerState()
    DatePickerDialog(
        onDismissRequest = hideDatePickerDialog,
        confirmButton = {
            TextButton(
                onClick = hideDatePickerDialog,
            ) {
                Text("OK")
            }
        },
        dismissButton = {
            TextButton(
                onClick = hideDatePickerDialog
            ) {
                Text("Cancel")
            }
        }
    ) {
        DatePicker(state = datePickerState)
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ModalBottomSheetComponent(
    bottomSheetState: SheetState,
    scope: CoroutineScope,
    hideModalBottomSheet: () -> Unit
) {
    ModalBottomSheet(
        onDismissRequest = hideModalBottomSheet,
        sheetState = bottomSheetState,
        scrimColor = Purple40Alpha30,

        ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center) {
            Button(
                // Note: If you provide logic outside of onDismissRequest to remove the sheet,
                // you must additionally handle intended state cleanup, if any.
                onClick = {
                    scope.launch { bottomSheetState.hide() }.invokeOnCompletion {
                        if (!bottomSheetState.isVisible) {
                            hideModalBottomSheet.invoke()
                        }
                    }
                }
            ) {
                Text("Hide Bottom Sheet")
            }
        }
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            content = {
                items(6) {
                    Text(
                        "Info Item ${it + 1}",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                    )
                }
            })
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun DatePickerComponent() {
    val datePickerState =
        rememberDatePickerState(initialSelectedDateMillis = System.currentTimeMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val date = Date().apply {
                        time = utcTimeMillis
                    }
                    val currentDate = Date()
                    return date.before(currentDate)
                }
            })

    DatePicker(
        state = datePickerState,
        modifier = Modifier.padding(16.dp),
        title = {
            Text(text = "Select start date")
        },
//        colors = DatePickerDefaults.colors(
//            containerColor = Purple80,
//            titleContentColor = Color.White,
//            selectedDayContainerColor = Purple40,
//            selectedDayContentColor = Color.White
//        ),
        headline = {
            DatePickerDefaults.DatePickerHeadline(
                selectedDateMillis = datePickerState.selectedDateMillis,
                displayMode = datePickerState.displayMode,
                dateFormatter = object : DatePickerFormatter {
                    override fun formatDate(
                        dateMillis: Long?,
                        locale: Locale,
                        forContentDescription: Boolean
                    ): String? {
                        return datePickerState.selectedDateMillis?.let {
                            android.icu.text.SimpleDateFormat.getInstanceForSkeleton("MMddyyyy")
                                .format(it)
                        } ?: android.icu.text.SimpleDateFormat.getInstanceForSkeleton("MMddyyyy")
                            .format(System.currentTimeMillis())
                    }

                    override fun formatMonthYear(
                        monthMillis: Long?,
                        locale: Locale
                    ): String? {
                        return null
                    }

                }
            )
        },
        showModeToggle = true,
    )
}

@Composable
private fun VerticalSpace() {
    Spacer(modifier = Modifier.height(32.dp))
}

@Composable
private fun BottomSheetContent(dismissBottomSheet: () -> Unit) {
    Box(
        Modifier
            .fillMaxWidth()
            .wrapContentHeight(), contentAlignment = Alignment.Center
    ) {
        Text("Swipe up to expand sheet")
    }
    Column(
        Modifier
            .fillMaxWidth()
            .fillMaxHeight()
            .padding(64.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Bottom Sheet content")
        Spacer(modifier = Modifier.height(120.dp))
        ElevatedButton(
            modifier = Modifier
                .wrapContentSize(),
            shape = RoundedCornerShape(4.dp),
            onClick = {
                dismissBottomSheet()
            }
        ) {
            Text(text = "Dismiss Bottom Sheet")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Material3DemoTheme {
        Material3Components()
    }
}