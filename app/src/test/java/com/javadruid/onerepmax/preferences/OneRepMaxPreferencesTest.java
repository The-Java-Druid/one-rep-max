package com.javadruid.onerepmax.preferences;

import static com.javadruid.onerepmax.preferences.OneRepMaxPreferences.KEY_SELECTED_ALGORITHM;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.SharedPreferences;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class OneRepMaxPreferencesTest {

    public static final String DEFAULT_ALGORITHM = "default_algorithm";
    @Mock
    SharedPreferences prefs;
    @Mock
    SharedPreferences.Editor editor;

    @Test
    public void getSavedAlgorithmName() {
        when(prefs.getString(KEY_SELECTED_ALGORITHM, "")).thenReturn(DEFAULT_ALGORITHM);
        final OneRepMaxPreferences instance = new OneRepMaxPreferences(prefs);

        assertEquals(DEFAULT_ALGORITHM, instance.getSavedAlgorithmName());
    }

    @Test
    public void setSavedAlgorithmName() {
        when(prefs.edit()).thenReturn(editor);
        when(editor.putString(same(KEY_SELECTED_ALGORITHM), any(String.class))).thenReturn(editor);
        final OneRepMaxPreferences instance = new OneRepMaxPreferences(prefs);

        instance.setSavedAlgorithmName("algorithm_name");

        verify(editor).apply();
    }
}