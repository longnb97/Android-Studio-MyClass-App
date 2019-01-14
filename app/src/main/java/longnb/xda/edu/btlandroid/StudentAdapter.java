package longnb.xda.edu.btlandroid;

import android.content.Context;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class StudentAdapter  extends RecyclerView.Adapter<StudentAdapter.ViewHolder> implements Filterable {
    ArrayList<StudentJSON> dataStudents;
    ArrayList<StudentJSON> dataStudentsBackUp;
    String oldFilter;
    Context context;
    private StudentAdapter listener;
    public StudentAdapter (ArrayList<StudentJSON> dataStudents, Context context){
        this.dataStudents = dataStudents;
        this.dataStudentsBackUp = dataStudents;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder( ViewGroup viewGroup, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());
        View itemView = layoutInflater.inflate(R.layout.student_row, viewGroup,false);
        ViewHolder holder = new ViewHolder(itemView);

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        viewHolder.studentName.setText(dataStudents.get(i).getFullname());
        viewHolder.averageScore.setText(dataStudents.get(i).getAverageScore());
        viewHolder.gender.setText(dataStudents.get(i).getGender());
        String imageUrl = dataStudents.get(i).getPhoto();
        Picasso.get()
                .load(imageUrl)
                .placeholder(R.drawable.img_loading)
                .into(viewHolder.photo);

        viewHolder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context ,StudentProfile.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.putExtra("classname", dataStudents.get(i).getClassname());
                intent.putExtra("image", dataStudents.get(i).getPhoto());
                intent.putExtra("name", dataStudents.get(i).getFullname());
                intent.putExtra("averageScore", dataStudents.get(i).getAverageScore());
                intent.putExtra("address", dataStudents.get(i).getAddress());
                intent.putExtra("dob", dataStudents.get(i).getDateOfBirth());
                intent.putExtra("conduct", dataStudents.get(i).getConduct());
                intent.putExtra("parentName", dataStudents.get(i).getParentName());
                intent.putExtra("phoneNumber", dataStudents.get(i).getPhoneNumber());
                intent.putExtra("email", dataStudents.get(i).getEmail());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return  dataStudents.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String filterString = constraint.toString();

                if(!filterString.isEmpty()) {
//                    if(!filterString.contains(oldFilter) ) {
//                        dataStudents = dataStudentsBackUp;
//                    }
                    oldFilter = filterString;
                    ArrayList<StudentJSON> listFilter = new  ArrayList<>();
                    for(StudentJSON item : dataStudentsBackUp) {
                        if(item.getFullname().toLowerCase().contains(filterString) || item.getGender().toLowerCase().contains(filterString)) {
                            listFilter.add(item);
                        }
                    }
                    FilterResults results = new FilterResults();
                    results.values = listFilter;
                    return  results;
                }
                FilterResults results = new FilterResults();
                results.values = dataStudentsBackUp;
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                dataStudents = (ArrayList<StudentJSON>) results.values;
                notifyDataSetChanged();
            }
        };
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView studentName;
        TextView averageScore;
        TextView gender;
        LinearLayout parentLayout;
        ImageView photo;

        public  ViewHolder(View itemView){
            super(itemView);
            parentLayout = itemView.findViewById(R.id.parentLayout);
            studentName = itemView.findViewById(R.id.studentName);
            averageScore = itemView.findViewById(R.id.averageScore);
            gender = itemView.findViewById(R.id.gender);
            photo = itemView.findViewById(R.id.photo);

        }
    }

    public void updateList (ArrayList<StudentJSON> newList){
        dataStudents = newList;
        dataStudentsBackUp = newList;
        notifyDataSetChanged();
    }
}
