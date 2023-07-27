package com.example.todolist

import androidx.compose.ui.test.*
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.todolist.data.TodoDatabase
import com.example.todolist.data.TodoRepository

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class ExampleInstrumentedTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule(MainActivity::class.java)
    @Test
    fun useAppContext() {
        // Context of the app under test.
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        assertEquals("com.example.todolist", appContext.packageName)
    }
    @Test
    fun initDatabase() {
        val database= TodoDatabase.getInstance(InstrumentationRegistry.getInstrumentation().targetContext)
        val repository= TodoRepository(database.todoDao())
        val mainViewModel= MainViewModel(repository)
        composeTestRule.setContent {
            NavigationHost(mainViewModel) { composeTestRule.activityRule.scenario.onActivity { activity ->
                activity.onBackPressedDispatcher.onBackPressed()
            } }
        }
        composeTestRule.waitUntil { composeTestRule.onAllNodesWithText("Task 1").fetchSemanticsNodes().size==4}
    }
    @Test
    fun testCRUD() {
        //create
        composeTestRule.onNode(hasTestTag("fab")).performClick()
        composeTestRule.onNodeWithText("Submit").performClick()
        composeTestRule.onNodeWithText("Please fill all the input fields").assertExists()
        composeTestRule.onNodeWithContentDescription("title").performTextInput("New Task")
        composeTestRule.onNodeWithContentDescription("description").performTextInput("Lorem ipsum...")
        composeTestRule.onNodeWithText("Date").performClick()
        onView(withId(android.R.id.button1)).perform(click())
        composeTestRule.onNodeWithText("Submit").performClick()
        composeTestRule.onRoot().performTouchInput { swipeUp() }
        composeTestRule.onNodeWithText("New Task").assertExists()
        //update
        composeTestRule.onNode(hasAnySibling(hasText("New Task")) and androidx.compose.ui.test.hasContentDescription("update")).performClick()
        composeTestRule.onNodeWithContentDescription("title").performTextInput(" Updated")
        composeTestRule.onNodeWithText("Submit").performClick()
        //delete
        composeTestRule.onNode(hasAnySibling(hasText("New Task Updated")) and androidx.compose.ui.test.hasContentDescription("delete")).performClick()
        composeTestRule.onNodeWithText("New Task Updated").assertDoesNotExist()
    }
}