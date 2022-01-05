package my.app.pecodetest

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

const val ARG_FRAGMENT_ID = "fragment_id"
const val INTENT_ACTION = "intent_open_fragment"

class MainFragment : Fragment() {

    val channelId = "channel_id_1"
    val notificationId = 0

    var fragmentId: Int = 1
    lateinit var fragmentNumberText: TextView
    lateinit var createNotificationButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_main, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragmentNumberText = view.findViewById(R.id.fragment_number_text)

        arguments?.takeIf { it.containsKey(ARG_FRAGMENT_ID) }?.apply {
            fragmentId = getInt(ARG_FRAGMENT_ID)
            fragmentNumberText.text = fragmentId.toString()
        }

        createNotificationChannel()

        createNotificationButton = view.findViewById(R.id.create_notification_button)
        createNotificationButton.setOnClickListener {
            createNotification()
        }

    }

    private fun createNotificationChannel() {
        val name = "Channel One"
        val description = "Notification numbers"
        val importance = NotificationManager.IMPORTANCE_DEFAULT

        val channel = NotificationChannel(channelId, name, importance)
        channel.description = description

        val notificationManager: NotificationManager = requireContext().getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun createNotification() {
        val intent = Intent(INTENT_ACTION).apply {
            putExtra(INTENT_ACTION, fragmentId)
        }
        val pendingIntent = PendingIntent.getBroadcast(requireActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT)

        val builder = NotificationCompat.Builder(requireContext(), channelId)
            .setContentTitle("Pecode Test App")
            .setContentText("Notification $fragmentId")
            .setSmallIcon(R.drawable.ic_baseline_message_24)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        NotificationManagerCompat.from(requireContext()).notify(notificationId + fragmentId, builder.build())
    }
}