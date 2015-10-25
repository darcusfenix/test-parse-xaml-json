package soy.crisostomo.app.test.activity.office365;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.common.util.concurrent.FutureCallback;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.SettableFuture;
import com.microsoft.aad.adal.AuthenticationCallback;
import com.microsoft.aad.adal.AuthenticationContext;
import com.microsoft.aad.adal.AuthenticationResult;
import com.microsoft.aad.adal.PromptBehavior;
import com.microsoft.services.orc.resolvers.ADALDependencyResolver;
import com.microsoft.services.outlook.Contact;
import com.microsoft.services.outlook.fetchers.OutlookClient;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import javax.crypto.NoSuchPaddingException;

import soy.crisostomo.app.test.R;

import static com.microsoft.aad.adal.AuthenticationResult.AuthenticationStatus;

;

public class Office365Activity extends Activity {

    private static final String outlookBaseUrl = "https://outlook.office365.com/api/v1.0";

    Button btnLogin;
    ListView listViewContacs;

    private AuthenticationContext mAuthContext;
    private ADALDependencyResolver mResolver;
    private OutlookClient outlookClient;
    private ArrayAdapter adapter;
    private List<String> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_office_365);

        // initialize adapter
        items = new ArrayList<String>();
        adapter = new ArrayAdapter<String>(this, R.layout.activity_office_365, items);

        // get references to controls
        btnLogin = (Button) findViewById(R.id.buttonLoginOffice);
        listViewContacs = (ListView) findViewById(R.id.listViewContacs);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Futures.addCallback(logon(), new FutureCallback<Boolean>() {
                    @Override
                    public void onSuccess(Boolean result) {
                        // initialize OotlookClient and get contacts
                        outlookClient = new OutlookClient(outlookBaseUrl, mResolver);
                        getContacts();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        Log.e("logon", t.getMessage());
                    }
                });
            }
        });
    }

    protected void getContacts() {
        Futures.addCallback(
                outlookClient.getMe()
                        .getContacts()
                        .read(),
                new FutureCallback<List<Contact>>() {
                    @Override
                    public void onSuccess(final List<Contact> result) {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                for (int i = 0; i < result.size(); i++)
                                    items.add(result.get(i).getDisplayName());
                                listViewContacs.setAdapter(adapter);
                            }
                        });
                    }

                    @Override
                    public void onFailure(final Throwable t) {
                        Log.e("getMessages", t.getMessage());
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        mAuthContext.onActivityResult(requestCode, resultCode, data);
    }

    protected SettableFuture<Boolean> logon() {
        final SettableFuture<Boolean> result = SettableFuture.create();

        try {
            mAuthContext = new AuthenticationContext(this, getString(R.string.AADAuthority), true);
            mAuthContext.acquireToken(
                    this,
                    getString(R.string.AADResourceId),
                    getString(R.string.AADClientId),
                    getString(R.string.AADRedirectUrl),
                    PromptBehavior.Auto,
                    new AuthenticationCallback<AuthenticationResult>() {

                        @Override
                        public void onSuccess(final AuthenticationResult authenticationResult) {
                            if (authenticationResult != null
                                    && authenticationResult.getStatus() == AuthenticationStatus.Succeeded) {
                                mResolver = new ADALDependencyResolver(
                                        mAuthContext,
                                        getString(R.string.AADResourceId),
                                        getString(R.string.AADClientId));
                                result.set(true);
                            }
                        }

                        @Override
                        public void onError(Exception e) {
                            result.setException(e);
                        }

                    });
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            e.printStackTrace();
            result.setException(e);
        }
        return result;
    }
}
