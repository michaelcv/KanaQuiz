package com.noprestige.kanaquiz.reference;

import android.content.Context;

import com.noprestige.kanaquiz.R;
import com.noprestige.kanaquiz.options.OptionsControl;
import com.noprestige.kanaquiz.questions.QuestionManagement;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

class ReferenceSubsectionPager extends FragmentPagerAdapter
{
    private int questionTypeRef;
    private List<Integer> tabList;
    Context context;

    private static List<String> pageIds;

    ReferenceSubsectionPager(FragmentManager fm, Context context, int questionTypeRef)
    {
        super(fm);
        this.questionTypeRef = questionTypeRef;
        this.context = context;

        tabList = new ArrayList<>(3);

        if (questionTypeRef == R.string.vocabulary)
        {
            boolean isFullReference = OptionsControl.getBoolean(R.string.prefid_full_reference);
            for (int i = 1; i <= QuestionManagement.getVocabulary().getCategoryCount(); i++)
                if (isFullReference || QuestionManagement.getVocabulary().getPref(i))
                    tabList.add(i);
        }
        else if (questionTypeRef == R.string.kanji)
        {
            boolean isFullReference = OptionsControl.getBoolean(R.string.prefid_full_reference);
            for (int i = 0; i < QuestionManagement.getKanjiFileCount(); i++)
                if (isFullReference || QuestionManagement.getKanji(i).anySelected())
                    tabList.add(i);
        }
        else if (OptionsControl.getBoolean(R.string.prefid_full_reference))
        {
            tabList.add(R.string.base_form_title);
            tabList.add(R.string.diacritics_title);
            tabList.add(R.string.digraphs_title);
            if (questionTypeRef == R.string.katakana)
                tabList.add(R.string.extended_katakana_title);
        }
        else
        {
            QuestionManagement questions;

            if (questionTypeRef == R.string.hiragana)
                questions = QuestionManagement.getHiragana();
            else if (questionTypeRef == R.string.katakana)
                questions = QuestionManagement.getKatakana();
            else
                throw new IllegalArgumentException("questionTypeRef '" + questionTypeRef + "' is invalid.");

            if (questions.anyMainKanaSelected())
                tabList.add(R.string.base_form_title);
            if (questions.diacriticsSelected())
                tabList.add(R.string.diacritics_title);
            if (questions.digraphsSelected())
                tabList.add(R.string.digraphs_title);
            if (questions.extendedKatakanaSelected())
                tabList.add(R.string.extended_katakana_title);
        }
    }

    @Override
    public Fragment getItem(int position)
    {
        if (questionTypeRef == R.string.vocabulary)
            return ReferenceSubsectionVocab
                    .newInstance(QuestionManagement.getVocabulary().getPrefId(tabList.get(position)));
        else
            return ReferenceSubsectionPage.newInstance(questionTypeRef, tabList.get(position));
    }

    @Override
    public long getItemId(int position)
    {
        if (questionTypeRef == R.string.vocabulary)
        {
            if (pageIds == null)
                pageIds = new ArrayList<>();
            String prefId = QuestionManagement.getVocabulary().getPrefId(tabList.get(position));
            if (!pageIds.contains(prefId))
                pageIds.add(prefId);
            return pageIds.indexOf(prefId);
        }
        if (questionTypeRef == R.string.kanji)
            //should clear out all pages if locale changes
            //no more than 16 kanji files, or this'll need to be modified
            return (Locale.getDefault().hashCode() << 4) + tabList.get(position);
        else
            return tabList.get(position);
    }

    @Override
    public int getCount()
    {
        return tabList.size();
    }

    @Override
    public CharSequence getPageTitle(int position)
    {
        if (questionTypeRef == R.string.vocabulary)
            return QuestionManagement.getVocabulary().getSetTitle(tabList.get(position)).toString();
        else if (questionTypeRef == R.string.kanji)
            return QuestionManagement.getKanjiTitle(tabList.get(position));
        else
            return context.getResources().getString(tabList.get(position));
    }
}
