package com.noprestige.kanaquiz.logs;

import android.arch.persistence.db.SupportSQLiteDatabase;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.arch.persistence.room.migration.Migration;
import android.content.Context;
import android.database.Cursor;

import java.util.Date;

@Database(entities = {DailyRecord.class, KanaRecord.class, IncorrectAnswerRecord.class}, version = 3)
@TypeConverters({LogTypeConversion.class})
public abstract class LogDatabase extends RoomDatabase
{
    public static LogDao DAO;

    protected abstract LogDao logDao();

    public static void initialize(Context context)
    {
        if (DAO == null)
            DAO = Room.databaseBuilder(context.getApplicationContext(), LogDatabase.class, "user-logs").
                    addMigrations(MIGRATION_1_2, MIGRATION_2_3).build().logDao();
    }

    public static final Migration MIGRATION_1_2 = new Migration(1, 2)
    {
        @Override
        public void migrate(SupportSQLiteDatabase database)
        {
            //rebuilding entire table because ALTER TABLE can't delete columns or change data types
            database.execSQL("ALTER TABLE daily_record RENAME TO old_daily_record");
            database.execSQL("CREATE TABLE IF NOT EXISTS daily_record (date INTEGER, correct_answers REAL NOT NULL, " +
                    "total_answers INTEGER NOT NULL, PRIMARY KEY(date))");

            Cursor cursor = database.query("SELECT * FROM old_daily_record");
            if (cursor.getCount() > 0)
            {
                int dateIndex = cursor.getColumnIndex("date");
                int correctIndex = cursor.getColumnIndex("correct_answers");
                int incorrectIndex = cursor.getColumnIndex("incorrect_answers");

                while (!cursor.isLast())
                {
                    cursor.moveToNext();

                    int dateValue = cursor.getInt(dateIndex);
                    int correctValue = cursor.getInt(correctIndex);
                    int incorrectValue = cursor.getInt(incorrectIndex);

                    database.execSQL("INSERT INTO daily_record (date, correct_answers, total_answers) VALUES (?, ?, ?)",
                            new Integer[]{dateValue, correctValue, (correctValue + incorrectValue)});
                }
            }
            database.execSQL("DROP TABLE old_daily_record");
        }
    };

    public static final Migration MIGRATION_2_3 = new Migration(2, 3)
    {
        @Override
        public void migrate(SupportSQLiteDatabase database)
        {
            int currentDate = LogTypeConversion.dateToTimestamp(new Date());

            //rebuilding tables because ALTER TABLE can't alter, or even add to, the primary key
            database.execSQL("ALTER TABLE kana_records RENAME TO old_kana_records");
            database.execSQL("ALTER TABLE incorrect_answers RENAME TO old_incorrect_answers");

            database.execSQL("CREATE TABLE IF NOT EXISTS kana_records (date INTEGER NOT NULL, kana TEXT NOT NULL, " +
                    "correct_answers INTEGER NOT NULL, incorrect_answers INTEGER NOT NULL, PRIMARY KEY(date, kana))");
            database.execSQL("CREATE TABLE IF NOT EXISTS incorrect_answers (date INTEGER NOT NULL, kana TEXT NOT " +
                    "NULL, incorrect_romanji TEXT NOT NULL, occurrences INTEGER NOT NULL, PRIMARY KEY(date, kana, " +
                    "incorrect_romanji))");


            Cursor kanaCursor = database.query("SELECT * FROM old_kana_records");
            if (kanaCursor.getCount() > 0)
            {
                int kanaKanaIndex = kanaCursor.getColumnIndex("kana");
                int kanaCorrectIndex = kanaCursor.getColumnIndex("correct_answers");
                int kanaIncorrectIndex = kanaCursor.getColumnIndex("incorrect_answers");

                while (!kanaCursor.isLast())
                {
                    kanaCursor.moveToNext();

                    String kanaKanaValue = kanaCursor.getString(kanaKanaIndex);
                    int kanaCorrectValue = kanaCursor.getInt(kanaCorrectIndex);
                    int kanaIncorrectValue = kanaCursor.getInt(kanaIncorrectIndex);

                    database.execSQL("INSERT INTO kana_records (date, kana, correct_answers, incorrect_answers) " +
                                    "VALUES (?, ?, ?, ?)",
                            new Object[]{currentDate, kanaKanaValue, kanaCorrectValue, kanaIncorrectValue});
                }
            }
            database.execSQL("DROP TABLE old_kana_records");


            Cursor incorrectCursor = database.query("SELECT * FROM old_incorrect_answers");
            if (incorrectCursor.getCount() > 0)
            {
                int incorrectKanaIndex = incorrectCursor.getColumnIndex("kana");
                int incorrectRomanjiIndex = incorrectCursor.getColumnIndex("incorrect_romanji");
                int incorrectOccurrencesIndex = incorrectCursor.getColumnIndex("occurrences");

                while (!incorrectCursor.isLast())
                {
                    incorrectCursor.moveToNext();

                    String incorrectKanaValue = incorrectCursor.getString(incorrectKanaIndex);
                    String incorrectRomanjiValue = incorrectCursor.getString(incorrectRomanjiIndex);
                    int incorrectOccurrencesValue = incorrectCursor.getInt(incorrectOccurrencesIndex);

                    database.execSQL("INSERT INTO incorrect_answers (date, kana, incorrect_romanji, occurrences) " +
                            "VALUES (?, ?, ?, ?)", new Object[]{
                            currentDate, incorrectKanaValue, incorrectRomanjiValue, incorrectOccurrencesValue
                    });
                }
            }
            database.execSQL("DROP TABLE old_incorrect_answers");
        }
    };
}
