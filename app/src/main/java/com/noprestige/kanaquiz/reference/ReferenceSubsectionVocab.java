package com.noprestige.kanaquiz.reference;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.noprestige.kanaquiz.R;
import com.noprestige.kanaquiz.questions.QuestionManagement;

import androidx.fragment.app.Fragment;

public class ReferenceSubsectionVocab extends Fragment
{
    private static final String ARG_VOCAB_SET_ID = "vocabSetId";

    public static ReferenceSubsectionVocab newInstance(String vocabSetId)
    {
        ReferenceSubsectionVocab screen = new ReferenceSubsectionVocab();
        Bundle args = new Bundle();
        args.putString(ARG_VOCAB_SET_ID, vocabSetId);
        screen.setArguments(args);
        return screen;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        String vocabSetId = getArguments().getString(ARG_VOCAB_SET_ID, "");

        View scrollBox = inflater.inflate(R.layout.fragment_reference_subsection_empty, container, false);
        ViewGroup layout = scrollBox.findViewById(R.id.secReference);

        for (int i = 1; i <= QuestionManagement.getVocabulary().getCategoryCount(); i++)
            if (vocabSetId.equals(QuestionManagement.getVocabulary().getPrefId(i)))
                layout.addView(QuestionManagement.getVocabulary().getVocabReferenceTable(container.getContext(), i));

        return scrollBox;
    }
}
