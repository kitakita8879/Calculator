package com.example.calculator;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.junit.Assert.assertEquals;

import android.content.Context;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class CalculatorInstrumentedTest {

    @Test
    public void useAppContext() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("com.example.calculator", appContext.getPackageName());
    }

    @Test
    public void keepsResultAfterActivityRecreation() {
        try (ActivityScenario<MainActivity> scenario = ActivityScenario.launch(MainActivity.class)) {
            onView(withId(R.id.btn_1)).perform(click());
            onView(withId(R.id.add)).perform(click());
            onView(withId(R.id.btn_2)).perform(click());
            onView(withId(R.id.equ)).perform(click());
            onView(withId(R.id.result)).check(matches(withText("3")));

            scenario.recreate();

            onView(withId(R.id.result)).check(matches(withText("3")));
        }
    }
}
