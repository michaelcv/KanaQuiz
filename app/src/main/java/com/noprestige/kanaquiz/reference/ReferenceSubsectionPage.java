package com.noprestige.kanaquiz.reference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noprestige.kanaquiz.R;
import com.noprestige.kanaquiz.options.OptionsControl;
import com.noprestige.kanaquiz.questions.QuestionManagement;

import androidx.fragment.app.Fragment;

public class ReferenceSubsectionPage extends Fragment
{
    private static final String ARG_QUESTION_TYPE_REF = "questionTypeRef";
    private static final String ARG_REF_CATEGORY_ID = "refCategoryId";

    public static ReferenceSubsectionPage newInstance(int questionTypeRef, int refCategoryId)
    {
        ReferenceSubsectionPage screen = new ReferenceSubsectionPage();
        Bundle args = new Bundle();
        args.putInt(ARG_QUESTION_TYPE_REF, questionTypeRef);
        args.putInt(ARG_REF_CATEGORY_ID, refCategoryId);
        screen.setArguments(args);
        return screen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        int questionTypeRef = getArguments().getInt(ARG_QUESTION_TYPE_REF, 0);
        int refCategoryId = getArguments().getInt(ARG_REF_CATEGORY_ID, 0);

        View scrollBox = inflater.inflate(R.layout.fragment_reference_subsection_empty, container, false);
        ViewGroup layout = scrollBox.findViewById(R.id.secReference);

        QuestionManagement questions;

        if (questionTypeRef == R.string.hiragana)
            questions = QuestionManagement.getHiragana();
        else if (questionTypeRef == R.string.katakana)
            questions = QuestionManagement.getKatakana();
        else if (questionTypeRef == R.string.kanji)
        {
            if (refCategoryId < QuestionManagement.getKanjiFileCount())
                layout.addView(
                        QuestionManagement.getKanji(refCategoryId).getKanjiReferenceTable(container.getContext()));
            return scrollBox;
        }
        else
            throw new IllegalArgumentException("questionTypeRef '" + questionTypeRef + "' is invalid.");

        if (refCategoryId == R.string.base_form_title)
            layout.addView(questions.getMainReferenceTable(container.getContext()));
        else if (refCategoryId == R.string.diacritics_title)
            layout.addView(questions.getDiacriticReferenceTable(container.getContext()));
        else if (refCategoryId == R.string.digraphs_title)
        {
            layout.addView(questions.getMainDigraphsReferenceTable(container.getContext()));
            if (OptionsControl.getBoolean(R.string.prefid_full_reference) || questions.diacriticDigraphsSelected())
            {
                layout.addView(
                        ReferenceCell.buildHeader(getContext(), getResources().getString(R.string.diacritics_title)));
                layout.addView(questions.getDiacriticDigraphsReferenceTable(container.getContext()));
            }
        }
        else if (refCategoryId == R.string.extended_katakana_title)
            layout.addView(questions.getExtendedKatakanaReferenceTable(container.getContext()));

        return scrollBox;
    }
}
