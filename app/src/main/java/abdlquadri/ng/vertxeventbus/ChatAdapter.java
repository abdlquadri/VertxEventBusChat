package abdlquadri.ng.vertxeventbus;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

        ArrayList chats;

        public ChatAdapter(ArrayList chats) {
            this.chats = chats;
        }

        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
            ChatViewHolder chatViewHolder = new ChatViewHolder(view);
            return chatViewHolder;
        }

        @Override
        public void onBindViewHolder(ChatViewHolder holder, int position) {

            Chat chat = (Chat) chats.get(position);
            holder.getTextView().setText(chat.getText());
            holder.getTimeView().setText(chat.getTime());
        }

        @Override
        public int getItemCount() {
            return chats.size();
        }

       public class ChatViewHolder extends RecyclerView.ViewHolder {
            TextView textView;

           public TextView getTimeView() {
               return timeView;
           }

           public void setTimeView(TextView timeView) {
               this.timeView = timeView;
           }

           TextView timeView;

            public TextView getTextView() {
                return textView;
            }

            public void setTextView(TextView textView) {
                this.textView = textView;
            }

            public ChatViewHolder(LinearLayout itemView) {
                super(itemView);
                TextView textView = (TextView) itemView.findViewById(R.id.chat);
                TextView timeView = (TextView) itemView.findViewById(R.id.time);
                setTextView(textView);
                setTimeView(timeView);
            }
        }
    }