package com.example.sih;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class jobcard extends AppCompatActivity {

    private static final String TAG = "jobcard";
    private final View jobView;
    List<job>  jobList;

    public jobcard(List<job> jobList) {
        this.jobList = jobList;
    }

    @NnNull
    @Override
    public job onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_job, parent, false);
        return new jobcard(view);
    }

    @Override
    public void onBindViewHolder(@NonNull job holder, int position) {

//        job job = jobList.get(position);
//        holder.titleTextView.setText(job);
//        holder.yearTextView.setText(job.);
//        holder.ratingTextView.setText(job);
//        holder.plotTextView.setText(job);

        boolean isExpanded = jobList.get(position).isExpanded();
        holder.expandableLayout.setVisibility(isExpanded ? View.VISIBLE : View.GONE);

    }

    @Override
    public int getItemCount() {
        return jobList.size();
    }

    class job extends RecyclerView.ViewHolder {

        private static final String TAG = "job";

        ConstraintLayout expandableLayout;
        TextView getJobTextView, jobTextView, ratingTextView, jobdescTextView,jobpostTextView,jobtype,jobskills,location,qualification,salary,sector;

        public jobList(@NonNull final View itemView) {
            super(itemView);

//            titleTextView = itemView.findViewById(R.id.titleTextView);
//            yearTextView = itemView.findViewById(R.id.yearTextView);
//            ratingTextView = itemView.findViewById(R.id.ratingTextView);
//            plotTextView = itemView.findViewById(R.id.plotTextView);
            expandableLayout = itemView.findViewById(R.id.expandableLayout);


            jobView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    jobList jobList = com.example.sih.jobList.get(getAdapterPosition());
                    job.setExpanded(!jobList.isExpanded());
                    notifyItemChanged(getAdapterPosition());

                }
            });
        }
    }
}
