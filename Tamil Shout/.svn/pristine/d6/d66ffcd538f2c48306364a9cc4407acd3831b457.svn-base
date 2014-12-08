package com.tamilshout;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.model.CountryBean;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.Utils;
import com.tamilshout.web.GetDataFromWebService;

public class CountryList extends BaseClass implements OnClickListener {

	private ListView listView;
	String response;
	ContanctAdapter objAdapter = null;
	private JSONObject jsonObject;
	private ProgressDialog pd;
	EditText search_edit_text;
	ArrayList<CountryBean> countries=null;
	private ArrayList<String> country = null;
	private ArrayList<CountryBean> countryList = null, temp;
	private ArrayList<String> states = null;

	ArrayList<Integer> checkedArrayList = new ArrayList<Integer>();

	TextView nameTxt, cmpnyNameTxt, phoneTypeTxt, phoneNoTxt;

	String countrStr = "", stateStr = "";
	Intent intent;

	int checkedPosition = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		super.setContentView(R.layout.country_list);

		setHeader("Select Country");
		setLeftButton(R.drawable.btn_back);

		// setTouchNClick(R.id.btn_login);

		setTouchNClick(R.id.btnTopLeft);
		


		listView = (ListView) findViewById(R.id.listView1);
		listView.setChoiceMode(AbsListView.CHOICE_MODE_SINGLE);
		listView.setFastScrollEnabled(true);

		getCountryList();

	}

	private void parsJsonObject(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {
			

			Type listType = new TypeToken<ArrayList<CountryBean>>() {
			}.getType();
			
			
			
			
			try {
				countries = new Gson().fromJson(new JSONObject(response).
						getJSONArray("responsepacketList").toString(), listType);
			} catch (JsonSyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			
		}

	}

	class ContanctAdapter extends ArrayAdapter<CountryBean> {

		private Activity activity;
		private List<CountryBean> items;
		private int row;
		private CountryBean objBean;

		public ContanctAdapter(Activity act, int row, List<CountryBean> items) {
			super(act, row, items);

			this.activity = act;
			this.row = row;
			this.items = items;

		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			View view = convertView;

			Log.d("In get View-------------", position + "");

			final ViewHolder holder;
			if (view == null) {
				LayoutInflater inflater = (LayoutInflater) activity
						.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				view = inflater.inflate(row, null);

				holder = new ViewHolder();
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			holder.countryNameTxt = (TextView) view
					.findViewById(R.id.countryNameTxt);
			holder.countryCodeTxt = (TextView) view
					.findViewById(R.id.countryCodeTxt);
			holder.checkBox = (CheckBox) view.findViewById(R.id.chkBox);
			holder.checkBox.setChecked(false);
			
			holder.countryNameTxt.setText(items.get(position).getCountryName());
			
			if(Constant.SELECTED_COUNTRY_NAME!=null && Constant.SELECTED_COUNTRY_NAME.equalsIgnoreCase(items.get(position).getCountryName()))
			{
				holder.checkBox.setChecked(true);
			}
			

			view.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					CheckBox box = (CheckBox) v.findViewById(R.id.chkBox);
					if (box.isChecked())
						box.setChecked(false);
					else
						box.setChecked(true);

					intent = getIntent();
					intent.putExtra("selectedCountry", ((TextView)(v.findViewById(R.id.countryNameTxt))).getText().toString());
					setResult(2, intent);
					finish();

				}

			});

			return view;
		}

		public class ViewHolder {
			public TextView countryNameTxt;
			public TextView countryCodeTxt;
			public CheckBox checkBox;

		}

	}

	private void getCountryList() {

	

		if (Utils.isNetworkAvailableNew(this)) {

			pd = ProgressDialog.show(this, "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			jsonObject = new JSONObject();
			try {

				new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService
								.readJsonFeed(getString(R.string.service_url)
										+ "CountryList", jsonObject.toString());

						runOnUiThread(new Runnable() {

							@Override
							public void run() {

								parsJsonObject(response);

								if (pd.isShowing())
									pd.dismiss();
								
								if(countries!=null && countries.size()>0)
								{
								listView.setAdapter(null);
								objAdapter = new ContanctAdapter(CountryList.this,
										R.layout.country_row, countries);
								listView.setAdapter(objAdapter);
								objAdapter.notifyDataSetChanged();
								}

							}

						});

					}
				}).start();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else
			Utils.showDialog(this, getString(R.string.no_network_available));

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btnTopLeft:
			finish();
			
			break;

		default:
			break;
		}

	}

}
