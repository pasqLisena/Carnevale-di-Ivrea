package it.polito.applicazionimultimediali.carnevalediivrea.minigame;

import it.polito.applicazionimultimediali.carnevalediivrea.GlobalRes;
import it.polito.applicazionimultimediali.carnevalediivrea.R;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.GamesActivityResultCodes;
import com.google.android.gms.games.request.GameRequest;
import com.google.android.gms.games.request.GameRequestBuffer;
import com.google.android.gms.games.request.OnRequestReceivedListener;
import com.google.android.gms.games.request.Requests;
import com.google.android.gms.games.request.Requests.LoadRequestsResult;
import com.google.android.gms.games.request.Requests.UpdateRequestsResult;
import com.google.example.games.basegameutils.BaseGameActivity;

public class FagiolataPlay extends BaseGameActivity implements OnClickListener {
	public OnRequestReceivedListener mRequestListener;
	public ResultCallback<LoadRequestsResult> mLoadRequestsCallback;

	private static boolean DEBUG_ENABLED = true;
	private static final String TAG = "FagiolataPlay";
	private static final int SHOW_INBOX = 1;
	private static final int SEND_GIFT_CODE = 2;
	private static final int SEND_REQUEST_CODE = 3;

	/** Default lifetime of a request, 1 week. */
	private static final int DEFAULT_LIFETIME = 7;

	/** Icon to be used to send gifts/requests */
	private Bitmap mGiftIcon;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		enableDebugLog(DEBUG_ENABLED);

		// Set up click listeners
		setContentView(R.layout.fagiolata_activity);
		findViewById(R.id.sign_in_button).setOnClickListener(this);
		findViewById(R.id.goBack).setOnClickListener(this);
		findViewById(R.id.button_open_inbox).setOnClickListener(this);
		findViewById(R.id.button_send_gift).setOnClickListener(this);
		findViewById(R.id.button_send_request).setOnClickListener(this);

		mGiftIcon = BitmapFactory.decodeResource(getResources(),
				R.drawable.orange);

		setTitle(getString(R.string.fagiolata_title));

		if (!isSignedIn()) {
			findViewById(R.id.sing_in_cont).setVisibility(View.VISIBLE);
			findViewById(R.id.console_cont).setVisibility(View.GONE);
		} else {
			findViewById(R.id.sing_in_cont).setVisibility(View.GONE);
			findViewById(R.id.console_cont).setVisibility(View.VISIBLE);
		}
		getGameHelper().setMaxAutoSignInAttempts(0);
	}

	private void updateRequestCounts() {
		PendingResult<Requests.LoadRequestsResult> result = Games.Requests
				.loadRequests(getApiClient(),
						Requests.REQUEST_DIRECTION_INBOUND,
						GameRequest.TYPE_ALL,
						Requests.SORT_ORDER_EXPIRING_SOON_FIRST);
		result.setResultCallback(mLoadRequestsCallback);
	}

	@Override
	public void onSignInFailed() {
		findViewById(R.id.sing_in_cont).setVisibility(View.VISIBLE);
		findViewById(R.id.console_cont).setVisibility(View.GONE);
	}

	/**
	 * Called to notify us that sign in succeeded. We react by loading the loot
	 * from the cloud and updating the UI to show a sign-out button.
	 */
	@Override
	public void onSignInSucceeded() {
		GlobalRes.getCurrentPlayer().updateInfo(getApiClient());

		findViewById(R.id.sing_in_cont).setVisibility(View.GONE);
		findViewById(R.id.console_cont).setVisibility(View.VISIBLE);

		if (mRequestListener == null) {
			mRequestListener = new OnRequestReceivedListener() {
				@Override
				public void onRequestReceived(GameRequest request) {
					int requestStringResource;
					switch (request.getType()) {
					case GameRequest.TYPE_GIFT:
						requestStringResource = R.string.new_gift_received;
						break;
					case GameRequest.TYPE_WISH:
						requestStringResource = R.string.new_request_received;
						break;
					default:
						return;
					}
					Toast.makeText(FagiolataPlay.this, requestStringResource,
							Toast.LENGTH_LONG).show();
					updateRequestCounts();
				}

				@Override
				public void onRequestRemoved(String requestId) {
				}
			};
		}

		if (mLoadRequestsCallback == null) {
			mLoadRequestsCallback = new ResultCallback<Requests.LoadRequestsResult>() {

				@Override
				public void onResult(LoadRequestsResult result) {
					int giftCount = 0;
					int wishCount = 0;
					GameRequestBuffer buf;
					buf = result.getRequests(GameRequest.TYPE_GIFT);
					if (null != buf) {
						giftCount = buf.getCount();
					}
					buf = result.getRequests(GameRequest.TYPE_WISH);
					if (null != buf) {
						wishCount = buf.getCount();
					}
					// Update the counts in the layout
					((TextView) findViewById(R.id.tv_gift_count))
							.setText(getResources().getQuantityString(
									R.plurals.gift, giftCount, giftCount));
					((TextView) findViewById(R.id.tv_request_count))
							.setText(getResources().getQuantityString(
									R.plurals.request, wishCount, wishCount));
				}

			};
		}

		Games.Requests
				.registerRequestListener(getApiClient(), mRequestListener);

		// Get any pending requests from the connection bundle
		ArrayList<GameRequest> requests = getGameHelper().getRequests();

		if (requests != null) {
			Log.d(TAG, "===========\nReqests count " + requests.size());
		} else {
			Log.d(TAG, "===========\nReqests are null");
		}
		// Use regular handler
		handleRequests(requests);
		// Make sure you don't handle these requests twice
		getGameHelper().clearRequests();

		// Our sample displays the request counts.
		updateRequestCounts();
	}

	/**
	 * Show a send gift or send wish request using startActivityForResult.
	 * 
	 * @param type
	 *            the type of GameRequest (gift or wish) to show
	 */
	private void showSendIntent(int type) {
		// Make sure we have a valid API client.
		if (getGameHelper().isSignedIn()) {
			GoogleApiClient client = getApiClient();
			if (!client.isConnected()) {
				Log.i(TAG,
						"Failed to show send intent, Google API client isn't connected!");
				return;
			}

			String description = "";
			int intentCode;
			Bitmap icon;
			switch (type) {
			case GameRequest.TYPE_GIFT:
				description = getString(R.string.send_gift_description);
				intentCode = SEND_GIFT_CODE;
				icon = mGiftIcon;
				break;
			case GameRequest.TYPE_WISH:
				description = getString(R.string.send_request_description);
				intentCode = SEND_REQUEST_CODE;
				icon = mGiftIcon;
				break;
			default:
				return;
			}
			Intent intent = Games.Requests.getSendIntent(client, type,
					"".getBytes(), DEFAULT_LIFETIME, icon, description);
			startActivityForResult(intent, intentCode);
		}
	}

	private String getRequestsString(ArrayList<GameRequest> requests) {
		if (requests.size() == 0) {
			return getString(R.string.accept0);
		}

		if (requests.size() == 1) {
			return String.format(getString(R.string.accept1), requests.get(0)
					.getSender().getDisplayName());
		}

		StringBuffer retVal = new StringBuffer(getString(R.string.acceptMore));

		for (GameRequest request : requests) {
			int type = request.getType() == GameRequest.TYPE_GIFT ? R.plurals.gift
					: R.plurals.request;
			retVal.append(String.format(getString(R.string.acceptMoreLi),
					getResources().getQuantityString(type, 1, 1),
					requests.get(0).getSender().getDisplayName()));
		}

		return retVal.toString();
	}

	// Actually accepts the requests
	private void acceptRequests(ArrayList<GameRequest> requests) {
		showLoader(true);
		// Attempt to accept these requests.
		ArrayList<String> requestIds = new ArrayList<String>();

		// Make sure we have a valid API client.
		GoogleApiClient client = getApiClient();

		/**
		 * Map of cached game request ID to its corresponding game request
		 * object.
		 */
		final HashMap<String, GameRequest> gameRequestMap = new HashMap<String, GameRequest>();

		// Cache the requests.
		for (GameRequest request : requests) {
			String requestId = request.getRequestId();
			requestIds.add(requestId);
			gameRequestMap.put(requestId, request);

			Log.d(TAG, "Processing request " + requestId);
		}
		// Accept the requests.
		Games.Requests.acceptRequests(client, requestIds).setResultCallback(
				new ResultCallback<UpdateRequestsResult>() {
					@Override
					public void onResult(UpdateRequestsResult result) {
						int numGifts = 0;
						int numRequests = 0;
						// Scan each result outcome.
						for (String requestId : result.getRequestIds()) {
							// We must have a local cached copy of the request
							// and the request needs to be a
							// success in order to continue.
							if (!gameRequestMap.containsKey(requestId)
									|| result.getRequestOutcome(requestId) != Requests.REQUEST_UPDATE_OUTCOME_SUCCESS) {
								continue;
							}
							// Update succeeded here. Find the type of request
							// and act accordingly. For wishes, a
							// responding gift will be automatically sent.
							switch (gameRequestMap.get(requestId).getType()) {
							case GameRequest.TYPE_GIFT:
								// Toast the player!
								++numGifts;
								break;
							case GameRequest.TYPE_WISH:
								++numRequests;
								break;
							}
						}

						if (numGifts != 0) {
							int gainedOranges = numGifts * 15;
							GlobalRes.getCurrentPlayer().gainOranges(
									gainedOranges);
							Toast.makeText(
									FagiolataPlay.this,
									String.format(
											getString(R.string.gift_gained_msg),
											gainedOranges), Toast.LENGTH_LONG)
									.show();
						}
						if (numGifts != 0 || numRequests != 0) {
							updateRequestCounts();
						}
						showLoader(false);
					}
				});

	}

	// Deal with any requests that are incoming, either from a bundle from the
	// app starting via notification, or from the inbox. Players should give
	// explicit approval to accept any gift or request, so we pop up a dialog.
	private void handleRequests(ArrayList<GameRequest> requests) {
		if (requests == null) {
			return;
		}

		// Must have final for anonymous function
		final ArrayList<GameRequest> theRequests = requests;

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(getRequestsString(requests))
				.setPositiveButton(getString(R.string.accept),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								acceptRequests(theRequests);
							}
						})
				.setNegativeButton(getString(R.string.refuse),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								// Do nothing---requests will remain un-created.
							}
						});
		// Create the AlertDialog object and return it
		builder.create().show();
	}

	// Response to inbox check
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case SEND_REQUEST_CODE:
			if (resultCode == GamesActivityResultCodes.RESULT_SEND_REQUEST_FAILED) {
				Toast.makeText(this, "FAILED TO SEND REQUEST!",
						Toast.LENGTH_LONG).show();
			}
			break;
		case SEND_GIFT_CODE:
			if (resultCode == GamesActivityResultCodes.RESULT_SEND_REQUEST_FAILED) {
				Toast.makeText(this, "FAILED TO SEND GIFT!", Toast.LENGTH_LONG)
						.show();
			}
			break;
		case SHOW_INBOX:
			if (resultCode == Activity.RESULT_OK && data != null) {
				handleRequests(Games.Requests
						.getGameRequestsFromInboxResponse(data));
			} else {
				Log.e(TAG, "Failed to process inbox result: resultCode = "
						+ resultCode + ", data = "
						+ (data == null ? "null" : "valid"));
			}
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void showLoader(boolean show) {
		int vis;
		if (show)
			vis = View.VISIBLE;
		else
			vis = View.GONE;
		findViewById(R.id.loader_cont).setVisibility(vis);
		;
	}

	@Override
	public void onClick(View view) {
		Log.d("AAAA", getResources().getResourceEntryName(view.getId()));

		int id = view.getId();
		if (id == R.id.sign_in_button) {
			beginUserInitiatedSignIn();
		} else if (id == R.id.goBack) {
			finish();
		} else if (id == R.id.button_send_gift) {
			showSendIntent(GameRequest.TYPE_GIFT);
		} else if (id == R.id.button_send_request) {
			showSendIntent(GameRequest.TYPE_WISH);
		} else if (id == R.id.button_send_request) {
			showSendIntent(GameRequest.TYPE_WISH);
		} else if (id == R.id.button_open_inbox || id ==  R.id.button_open_inbox2) {
			if (getGameHelper().isSignedIn()) {
				startActivityForResult(
						Games.Requests.getInboxIntent(getApiClient()),
						SHOW_INBOX);
			}
		}
		
	}

}
