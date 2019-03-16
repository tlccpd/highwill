package kr.ac.gwnu.ar.ui;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import kr.ac.gwnu.R;
public class ListDialog extends Dialog {
	private Button   list_dialog_close;
	private ListView list_dialog_view;

	private List<String> listItem;

	private ListDialogClickListener listDialogClickListener;

	public ListDialog(Context context) {
		super(context);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		setContentView(R.layout.list_dialog);

		list_dialog_close = (Button) findViewById(R.id.list_dialog_close);
		list_dialog_view = (ListView) findViewById(R.id.list_dialog_view);

		list_dialog_view.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> arg0, View view,int position, long arg3) {
				listDialogClickListener.onClick(listItem.get(position),position);
			}
		});

		ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),android.R.layout.simple_list_item_1, listItem);
		list_dialog_view.setAdapter(adapter);

		list_dialog_close.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				cancel();
			}
		});

		DisplayMetrics metrics = getContext().getResources().getDisplayMetrics();

		int w = (int) (metrics.widthPixels / 1.2);
		int h = (int) (metrics.heightPixels / 1.2);
		
		setWidth(w, h);
	}
	
	public ListDialog setWidth(int w,int h){
		this.getWindow().setLayout(w,h);	
		
		return this;
	}

	public List<String> getListItem() {
		return listItem;
	}

	public void setListItem(List<String> listItem) {
		this.listItem = listItem;
	}

	public ListDialogClickListener getListDialogClickListener() {
		return listDialogClickListener;
	}

	public ListDialog setListDialogClickListener(ListDialogClickListener listDialogClickListener) {
		this.listDialogClickListener = listDialogClickListener;

		return this;
	}
}
