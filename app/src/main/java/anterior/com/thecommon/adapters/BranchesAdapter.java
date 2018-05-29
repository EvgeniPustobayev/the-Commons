package anterior.com.thecommon.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import anterior.com.thecommon.R;

/**
 * Created by admin on 21/09/2017.
 */

public class BranchesAdapter extends BaseAdapter{

    Context context;
    private static LayoutInflater inflater = null;

    public BranchesAdapter(Context context)
    {
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi=view;

        if(vi == null)
            vi = inflater.inflate(R.layout.branches_row,null);

        TextView txtTitle = (TextView)vi.findViewById(R.id.txtTitle);
        String[] arrayTypes = this.context.getResources().getStringArray(R.array.branches_type);
        txtTitle.setText(arrayTypes[i]);

        return vi;
    }
}
