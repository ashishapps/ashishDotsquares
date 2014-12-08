package com.tamilshout.fragments;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.tamilshout.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.tamilshout.HomeActivity;
import com.tamilshout.ImageZoomInZoomOutActivity;
import com.tamilshout.LoginActivity;
import com.tamilshout.model.BusinessUser;
import com.tamilshout.utils.AppPrefrence;
import com.tamilshout.utils.Constant;
import com.tamilshout.utils.ImageLoader;
import com.tamilshout.utils.Utils;
import com.tamilshout.utils.ImageLoader.ImageLoadedListener;
import com.tamilshout.web.GetDataFromWebService;

public class FavouriteFragment extends BaseFragment {

	boolean create = false;
	ArrayList<BusinessUser> categoryList;
	private ProgressDialog pd;
	String response;
	JSONObject jsonObject = null;

	ListView listView;
	/* DBHelper dbh; */
	ListView lvDrink;
	TextView txtNoItem;
	String catId = "";
	String seacrchKeyword = "";
	Bundle args;
	int pageNo = 1;
	ArrayList<BusinessUser> temp;
	private Button btnLoadMore;
	View v;
	Thread t1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onCreateView(inflater, container, savedInstanceState);

		v = inflater.inflate(R.layout.user_list_fragment, null);
		temp = new ArrayList<BusinessUser>();

		/*
		 * if (Constant.SELECTED_COUNTRY_NAME != null &&
		 * Constant.CURRENT_COUNTRY != null && Constant.SELECTED_COUNTRY_NAME
		 * .equalsIgnoreCase(Constant.CURRENT_COUNTRY)) { ((Button)
		 * v.findViewById(R.id.distanceBtn)) .setVisibility(View.VISIBLE); }
		 * else {
		 * 
		 * if (Constant.SELECTED_COUNTRY_NAME == null) { ((Button)
		 * v.findViewById(R.id.distanceBtn)) .setVisibility(View.VISIBLE); }
		 * else { ((Button) v.findViewById(R.id.distanceBtn))
		 * .setVisibility(View.INVISIBLE); }
		 * 
		 * }
		 */
		setHeader(v, "Favourites");
		// setLeftButton(v, R.drawable.btn_back);

		// setTouchNClick(v, R.id.btnTopLeft);
		setTouchNClick(v, R.id.alphaBtn);
		setTouchNClick(v, R.id.distanceBtn);

		listView = (ListView) v.findViewById(R.id.listView1);
		// LoadMore button
		btnLoadMore = new Button(getActivity());
		btnLoadMore.setText("Load More");

		// Adding Load More button to lisview at bottom
		listView.addFooterView(btnLoadMore);
		listView.setFooterDividersEnabled(false);
		btnLoadMore.setTextAppearance(getActivity(),
				android.R.style.TextAppearance_Medium);

		/*
		 * args = getArguments(); if (args != null && args.containsKey("catid"))
		 * catId = args.getString("catid"); if (args != null &&
		 * args.containsKey("isComesFromHome")) seacrchKeyword =
		 * args.getString("seacrchKeyWord");
		 */

		Constant.appContext = getActivity();
		Log.d("Home", "Category Lsit");

		btnLoadMore.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				pageNo = pageNo + 1;

				getFavouriteBusiness(1000, false);

			}
		});

		return v;
	}

	class BusinessNameComparator implements Comparator<BusinessUser> {

		// new Student()--> name:a,date:1999}
		// new Student()--> name:a,date:1998}
		// new Student()--> name:b,date:1910}

		// output:-
		// a-1998
		// a-1999
		// b-1910

		/*
		 * @Override public int compare(Student s, Student t) { int f =
		 * s.name.compareTo(t.name); return (f != 0) ? f :
		 * s.date.compareTo(t.date); }
		 */

		@Override
		public int compare(BusinessUser lhs, BusinessUser rhs) {
			// TODO Auto-generated method stub

			return lhs.getBusinessname().compareToIgnoreCase(
					rhs.getBusinessname());

		}
	}

	static class BusinessDistanceComparator implements Comparator<BusinessUser> {

		@Override
		public int compare(BusinessUser lhs, BusinessUser rhs) {
			// TODO Auto-generated method stub

			int f = lhs.getDistance().compareTo(rhs.getDistance());
			return f;
		}
	}

	@Override
	public void onResume() {

		if (listView == null) {
			listView = (ListView) v.findViewById(R.id.listView1);
			// LoadMore button
			btnLoadMore = new Button(getActivity());
			btnLoadMore.setText("Load More");

			// Adding Load More button to lisview at bottom
			listView.addFooterView(btnLoadMore);
			listView.setFooterDividersEnabled(false);
			btnLoadMore.setTextAppearance(getActivity(),
					android.R.style.TextAppearance_Medium);
		}

		if (!AppPrefrence.isLogin(getActivity())) {

			Utils.showDialog(getActivity(), getString(R.string.login_required),
					"", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

							Intent intent = new Intent(getActivity(),
									LoginActivity.class);
							intent.putExtra("isGoHome", true);
							startActivity(intent);
							getActivity().finish();

						}
					}, false).show();

		} else {
			
			if (Utils.isUserExistsOnServer(getActivity())) {
				
				pageNo = 1;
				if (categoryList != null)
					categoryList.clear();
				if (temp != null)
					temp.clear();

				getFavouriteBusiness(1000, false);

			}
			
			
		}

		super.onResume();

	}

	private void getFavouriteBusiness(double km, boolean shouldListClear) {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			pd = ProgressDialog.show(getActivity(), "", ""
					+ getString(R.string.processing));
			pd.setCancelable(true);

			try {

				jsonObject = new JSONObject();

				jsonObject.put("id", AppPrefrence.getProfile(getActivity())
						.getUserid());
				jsonObject.put("pageno", pageNo);

				t1 = new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "SelectFavouriteBusiness",
								jsonObject.toString());

						if (v != null) {
							getActivity().runOnUiThread(new Runnable() {

								@Override
								public void run() {

									if (categoryList != null) {
										categoryList.clear();
										categoryList = null;
									}

									parsJsonObject(response);

									if (categoryList != null
											&& categoryList.size() > 0) {

										if (temp.size() == 10) {
											listView.addFooterView(btnLoadMore);

										} else
											listView.removeFooterView(btnLoadMore);

										int currentPosition = listView
												.getFirstVisiblePosition();

										listView.setAdapter(null);
										BusinessUserAdapter objAdapter = new BusinessUserAdapter(
												getActivity(), categoryList);
										listView.setAdapter(objAdapter);

										// Appending new data to menuItems
										// ArrayList

										// Setting new scroll position

										if (pd != null && pd.isShowing())
											pd.dismiss();

									} else {
										if (pd != null && pd.isShowing())
											pd.dismiss();
										listView.removeFooterView(btnLoadMore);
										listView.setAdapter(null);
										Utils.showDialog(
												getActivity(),
												getString(R.string.no_record),
												"",
												new DialogInterface.OnClickListener() {

													public void onClick(
															DialogInterface dialog,
															int which) {
														/*
														 * ((HomeActivity)
														 * getActivity())
														 * .onBackPressed();
														 */
														dialog.dismiss();

													}

												}, true).show();

									}

								}

							});
						}

					}
				});
				t1.start();
			} catch (Exception exception) {
				if (pd != null && pd.isShowing())
					pd.dismiss();
				exception.printStackTrace();
			}
		} else
			Utils.showDialog(getActivity(),
					getString(R.string.no_network_available));

	}

	private void parsJsonObject(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			Type listType = new TypeToken<ArrayList<BusinessUser>>() {
			}.getType();

			try {

				if (temp != null)
					temp.clear();
				if (categoryList == null)
					categoryList = new ArrayList<BusinessUser>();

				temp = new Gson().fromJson(new JSONObject(response)
						.getJSONArray("responsepacketList").toString(),
						listType);

				categoryList.addAll(temp);

				/*
				 * categoryList = new Gson().fromJson(new JSONObject(response)
				 * .getJSONArray("responsepacketList").toString(), listType);
				 */
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Utils.showDialog(getActivity(), getString(R.string.server_error));
		}

	}

	private void parsJsonObjectForDelete(String response) {

		if (response != null && response.length() > 0
				&& response.startsWith("{")) {

			try {
				JSONObject jsonObject = new JSONObject(response);
				if (jsonObject != null
						&& jsonObject.getString("responsecode").equals("200")
						&& jsonObject.getString("status")
								.equalsIgnoreCase("OK")) {
					getFavouriteBusiness(1000, true);
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		} else {
			Utils.showDialog(getActivity(), getString(R.string.server_error));
		}

	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		super.onClick(v);

		switch (v.getId()) {
		case R.id.btnTopRight:
			// ((BaseContainerFragment)getParentFragment()).replaceFragment(new
			// AddDrinkFragment(), true);
			break;
		case R.id.btnTopLeft:

			((HomeActivity) getActivity()).onBackPressed();
			break;
		case R.id.alphaBtn:

			sortByBusinessName();
			break;
		case R.id.distanceBtn:

			final String names[] = { "1 MILE", "5 MILES", "10 MILES",
					"50 MILES", "100 MILES", "NATIONAL" };
			final AlertDialog.Builder alertDialog = new AlertDialog.Builder(
					getActivity());
			LayoutInflater inflater = getActivity().getLayoutInflater();
			View convertView = (View) inflater.inflate(
					R.layout.dialog_custom_titile, null);
			alertDialog.setView(convertView);
			alertDialog.setTitle("Sort y Distance");

			alertDialog.setCancelable(true);
			ListView lv = (ListView) convertView.findViewById(R.id.listView1);
			lv.setSelection(Constant.SELECTED_FILTER);
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(
					getActivity(), android.R.layout.simple_list_item_1, names);
			lv.setAdapter(adapter);
			final AlertDialog ad = alertDialog.show();

			lv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					ad.dismiss();
					int miles = 0;
					if (!names[arg2].split(" ")[0].equalsIgnoreCase("national")) {
						miles = Integer.parseInt(names[arg2].split(" ")[0]);

					} else {
						miles = 1000;
					}

					pageNo = 1;

					if (categoryList != null)
						categoryList.clear();
					getFavouriteBusiness(miles, false);
				}
			});

			// sortByDistance();
			break;

		default:
			break;
		}

	}

	private void sortByDistance() {
		Collections.sort(categoryList, new BusinessDistanceComparator());
		listView.setAdapter(null);
		BusinessUserAdapter objAdapter = new BusinessUserAdapter(getActivity(),
				categoryList);
		listView.setAdapter(objAdapter);
		objAdapter.notifyDataSetChanged();
	}

	private void sortByBusinessName() {
		if (categoryList != null && categoryList.size() > 0) {
			Collections.sort(categoryList, new BusinessNameComparator());
			listView.setAdapter(null);
			BusinessUserAdapter objAdapter = new BusinessUserAdapter(
					getActivity(), categoryList);
			listView.setAdapter(objAdapter);
			objAdapter.notifyDataSetChanged();
		}
	}

	private class BusinessUserAdapter extends BaseAdapter {
		Context context;
		ArrayList<BusinessUser> arrayList;

		ImageLoader imageLoader = new ImageLoader();

		public BusinessUserAdapter(Context context,
				ArrayList<BusinessUser> arrayList) {
			this.context = context;
			this.arrayList = arrayList;

		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return arrayList.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return arrayList.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(final int position, View convertView,
				ViewGroup parent) {
			// TODO Auto-generated method stub

			View row = convertView;

			if (row == null) {
				row = getActivity().getLayoutInflater().inflate(
						R.layout.business_user_item, parent, false);
			}

			TextView businessName = (TextView) row
					.findViewById(R.id.business_title_Txt);
			TextView disttance = (TextView) row.findViewById(R.id.distance_Txt);
			TextView addressLabel = (TextView) row
					.findViewById(R.id.addressTxt);
		
			TextView phoneNumber = (TextView) row.findViewById(R.id.phone_txt);
			/*
			 * String addressStr = ""; if (arrayList.get(position).getCity() !=
			 * null) { addressStr = arrayList.get(position).getCity();
			 * 
			 * } if (arrayList.get(position).getState() != null) { addressStr =
			 * "," + arrayList.get(position).getState();
			 * 
			 * }
			 */

			businessName.setText(arrayList.get(position).getBusinessname());

			disttance.setVisibility(View.GONE);
			/*
			 * if (Constant.SELECTED_COUNTRY_NAME != null &&
			 * Constant.CURRENT_COUNTRY != null &&
			 * Constant.SELECTED_COUNTRY_NAME .equals(Constant.CURRENT_COUNTRY))
			 * {
			 * 
			 * disttance.setVisibility(View.VISIBLE); double dist =
			 * Utils.roundMyData( Double.parseDouble(arrayList.get(position)
			 * .getDistance().toString()), 2); disttance.setText(dist + " km");
			 * } else {
			 * 
			 * if (Constant.SELECTED_COUNTRY_NAME == null) {
			 * disttance.setVisibility(View.VISIBLE); double dist =
			 * Utils.roundMyData( Double.parseDouble(arrayList.get(position)
			 * .getDistance().toString()), 2); disttance.setText(dist + " km");
			 * } else { disttance.setVisibility(View.GONE); }
			 * 
			 * }
			 */
			
			
			String addressStr = "";
			if (arrayList.get(position).getAddress1() != null
					&& arrayList.get(position).getAddress1().length() > 0)
				addressStr = arrayList.get(position).getAddress1();
			if (arrayList.get(position).getAddress2() != null
					&& arrayList.get(position).getAddress2().length() > 0)
				addressStr = addressStr + ", "
						+ arrayList.get(position).getAddress2();

			if (addressStr.startsWith(","))
				addressStr = addressStr.substring(1);
			if (addressStr.endsWith(","))
				addressStr = addressStr.substring(0, addressStr.length() - 1);

			addressLabel.setText(addressStr.trim());
			
			//addressData.setText(arrayList.get(position).getAddress2());
			phoneNumber.setText(arrayList.get(position).getContactno());
			phoneNumber.setVisibility(View.GONE);

			Button arrowImg = (Button) row.findViewById(R.id.arrowImg);
			Button websiteImg = (Button) row.findViewById(R.id.websiteImg);
			Button messageImg = (Button) row.findViewById(R.id.messageImg);
			Button addImg = (Button) row.findViewById(R.id.addImg);

			row.setTag(position);

			if (categoryList.get(position).isFlag_businessad()) {
				addImg.setVisibility(View.VISIBLE);
			} else {
				addImg.setVisibility(View.GONE);
			}

			if (categoryList.get(position).getWebsite() != null
					&& categoryList.get(position).getWebsite().length() <= 0)
				websiteImg.setVisibility(View.GONE);
			else
				websiteImg.setVisibility(View.VISIBLE);

			if (categoryList.get(position).getContactemail() != null
					&& categoryList.get(position).getContactemail().length() <= 0)
				messageImg.setVisibility(View.GONE);
			else
				messageImg.setVisibility(View.VISIBLE);

			websiteImg.setTag("" + categoryList.get(position).getWebsite());
			messageImg
					.setTag("" + categoryList.get(position).getContactemail());

			String timeStamp = categoryList.get(position).getTime();
			String imgUrl = getString(R.string.image_url) + "User/"
					+ categoryList.get(position).getId() + "/medium.jpg?id="
					+ timeStamp;

			addImg.setTag("" + position);

			addImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String webUrl = "", addBannerImgUrl = "";
					if (categoryList
							.get(Integer.parseInt(v.getTag().toString()))
							.getBusinessAddType().equals("1")) {
						webUrl = getString(R.string.image_url)
								+ "UserCoupen/CoupenImage/"
								+ categoryList
										.get(Integer.parseInt(v.getTag()
												.toString())).getId()
								+ "/original.jpg?id="
								+ categoryList
										.get(Integer.parseInt(v.getTag()
												.toString())).getTime();
						addBannerImgUrl = getString(R.string.image_url)
								+ "UserCoupen/CoupenImage/"
								+ categoryList
										.get(Integer.parseInt(v.getTag()
												.toString())).getId()
								+ "/original.jpg?id="
								+ categoryList
										.get(Integer.parseInt(v.getTag()
												.toString())).getTime();

						Intent intent = new Intent(getActivity(),
								ImageZoomInZoomOutActivity.class);
						intent.putExtra("imgUrl", webUrl);
						startActivity(intent);

					} else {
						webUrl = getString(R.string.image_url)
								+ "UserCoupen/CoupenVideo/"
								+ categoryList.get(position).getId()
								+ "/Video_"
								+ categoryList
										.get(Integer.parseInt(v.getTag()
												.toString())).getId() + ".mp4";
						addBannerImgUrl = getString(R.string.image_url)
								+ "UserCoupen/CoupenVideo/"
								+ categoryList.get(position).getId()
								+ "/medium_"
								+ categoryList
										.get(Integer.parseInt(v.getTag()
												.toString())).getId() + ".jpg";

						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(webUrl));
						intent.setDataAndType(Uri.parse(webUrl), "video/mp4");
						startActivity(intent);
					}

				}
			});

			final ImageView business_img = (ImageView) row
					.findViewById(R.id.business_img);

			if (imgUrl.length() > 0
					&& (imgUrl.contains(".jpg") || imgUrl.contains(".gif")
							|| imgUrl.contains(".png") || imgUrl
								.contains(".jpeg"))) {

				Bitmap bitmap = imageLoader.loadImage(imgUrl,
						new ImageLoadedListener() {

							@Override
							public void imageLoaded(Bitmap imageBitmap) {

								if (imageBitmap != null) {
									business_img.setImageBitmap(imageBitmap);

									notifyDataSetChanged();
								}
								// TODO Auto-generated method stub

							}
						});
				if (bitmap != null)
					business_img.setImageBitmap(bitmap);
				else
					business_img.setImageDrawable(getResources().getDrawable(
							R.drawable.no_image));
			}

			// imgImageView.setVisibility(View.VISIBLE);
			// imgImageView.setBackgroundResource(R.drawable.merchandise_normal);

			websiteImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					String url = v.getTag().toString();

					Utils.openWebsite(getActivity(), url);

				}
			});
			messageImg.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					String emailId = v.getTag().toString();

					Utils.sendMail(getActivity(), emailId);

				}
			});

			row.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {

					if (Utils.isOnline()) {
						FavouriteBusinessUserProfile fragment = new FavouriteBusinessUserProfile();
						Bundle bundle = new Bundle();
						bundle.putString(
								"userid",
								arrayList
										.get(Integer.parseInt(v.getTag()
												.toString())).getId());
						fragment.setArguments(bundle);
						((FavouriteContainerFragment) getParentFragment())
								.replaceFragment(fragment, true, "Profile");
					} else {
						Utils.showDialog(getActivity(),
								getString(R.string.no_network_available));
					}

				}
			});

			row.setOnLongClickListener(new OnLongClickListener() {

				@Override
				public boolean onLongClick(View v) {
					// TODO Auto-generated method stub
					int position = (Integer) v.getTag();
					showLongPressOptions(position);
					return false;
				}
			});

			return row;
		}
	}

	public void showLongPressOptions(final int id) {
		String[] items = { "Remove Business", "Cancel" };
		AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Select one");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				// Do something with the selection
				if (item == 0) {
					Utils.showDialog(getActivity(), "Are you sure!",
							"Do you want to remove "
									+ categoryList.get(id).getBusinessname()
									+ " from favourite?", "Yes", "No",
							new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// TODO Auto-generated method stub

									deleteMyItems(id);

									/*
									 * dbHandler.removeFromFav(clubList.get(id).
									 * getId()); setClubList();
									 */
								}
							}, new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									dialog.dismiss();

								}
							});

				}
				if (item == 1) {
					dialog.dismiss();
				}
			}
		});
		AlertDialog alert = builder.create();
		alert.show();
	}

	private void deleteMyItems(int id) {

		if (Utils.isNetworkAvailableNew(getActivity())) {

			/*
			 * pd = ProgressDialog.show(getActivity(), "", "" +
			 * getString(R.string.processing)); pd.setCancelable(true);
			 */

			try {

				jsonObject = new JSONObject();

				jsonObject.put("appuserid",
						AppPrefrence.getProfile(getActivity()).getUserid());
				jsonObject.put("buserid", categoryList.get(id).getId());

				new Thread(new Runnable() {

					public void run() {

						response = GetDataFromWebService.readJsonFeed(
								getString(R.string.service_url)
										+ "DeleteFavouriteBusiness",
								jsonObject.toString());

						getActivity().runOnUiThread(new Runnable() {

							@Override
							public void run() {

								parsJsonObjectForDelete(response);

								if (pd.isShowing())
									pd.dismiss();
							}

						});

					}
				}).start();
			} catch (Exception exception) {
				exception.printStackTrace();
			}
		} else
			Utils.showDialog(getActivity(),
					getString(R.string.no_network_available));

	}

	@Override
	public void onDestroyView() {
		// TODO Auto-generated method stub
		// dbh.closeDB();
		super.onDestroyView();
		if (pd != null && pd.isShowing())
			pd.dismiss();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();

		if (pd != null && pd.isShowing())
			pd.dismiss();

		if (t1 != null)
			t1.interrupt();

		Thread.currentThread().interrupt();

		v = null;

	}
}
