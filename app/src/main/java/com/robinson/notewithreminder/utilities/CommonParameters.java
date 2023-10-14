package com.robinson.notewithreminder.utilities;


public class CommonParameters {

    public static String noteId = "noteId";
    public static String noteTitle = "noteTitle";
    public static String noteDescription = "noteDescription";
    public static String noteDate = "noteDate";
    public static String noteTime = "noteTime";
    public static String isUpdate = "isUpdate";


    /*// Private Function to create the OneTimeWorkRequest
    public static void createWorkRequest(Context context,
                                         String id,
                                         String title,
                                         String message,
                                         String time,
                                         String date,
                                         long timeDelayInSeconds) {
        OneTimeWorkRequest myWorkRequest = new OneTimeWorkRequest.Builder(ReminderWorker.class)
                .setInitialDelay(timeDelayInSeconds, TimeUnit.SECONDS)
                .setInputData(
                        new Data.Builder()
                                .putString(noteId, id)
                                .putString(noteTitle, title)
                                .putString(noteDescription, message)
                                .putString(noteDate, date)
                                .putString(noteTime, time)
                                .build()
                )
                .build();

        WorkManager.getInstance(context).enqueue(myWorkRequest);
    }
*/
}
