package com.algorand.Tutorials;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.Map.Entry;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.algod.client.AlgodClient;
import com.algorand.algosdk.algod.client.ApiException;
import com.algorand.algosdk.algod.client.api.AlgodApi;
import com.algorand.algosdk.algod.client.auth.ApiKeyAuth;
import com.algorand.algosdk.algod.client.model.AssetHolding;
import com.algorand.algosdk.algod.client.model.AssetParams;
import com.algorand.algosdk.algod.client.model.TransactionID;
import com.algorand.algosdk.algod.client.model.TransactionParams;
import com.algorand.algosdk.crypto.Address;
import com.algorand.algosdk.crypto.Digest;
import com.algorand.algosdk.transaction.SignedTransaction;
import com.algorand.algosdk.transaction.Transaction;
import com.algorand.algosdk.util.Encoder;

/**
 * Show Creating, modifying, sending and listing assets
 */
public class TutorialASA {
    // Inline class to handle changing block parameters
    // Throughout the example
    static class ChangingBlockParms {
        public BigInteger fee;
        public BigInteger firstRound;
        public BigInteger lastRound;
        public String genID;
        public Digest genHash;

        public ChangingBlockParms() {
            this.fee = BigInteger.valueOf(0);
            this.firstRound = BigInteger.valueOf(0);
            this.lastRound = BigInteger.valueOf(0);
            this.genID = "";
            this.genHash = null;
        }
    };

    // Utility function to wait on a transaction to be confirmed
    private static void waitForTransactionToComplete(final AlgodApi algodApiInstance, final String txID)
            throws Exception {
        while (true) {
            try {
                // Check the pending tranactions
                final com.algorand.algosdk.algod.client.model.Transaction b3 = algodApiInstance
                        .pendingTransactionInformation(txID);
                if (b3.getRound() != null && b3.getRound().longValue() > 0) {
                    // Got the completed Transaction
                    System.out
                            .println("Transaction " + b3.getTx() + " confirmed in round " + b3.getRound().longValue());
                    break;
                }
            } catch (final Exception e) {
                throw (e);
            }
        }

    }

    // Utility function to update changing block parameters
    public static ChangingBlockParms getChangingParms(final AlgodApi algodApiInstance) throws Exception {
        final ChangingBlockParms cp = new TutorialASA.ChangingBlockParms();
        try {
            final TransactionParams params = algodApiInstance.transactionParams();
            cp.fee = params.getFee();
            cp.firstRound = params.getLastRound();
            cp.lastRound = cp.firstRound.add(BigInteger.valueOf(1000));
            cp.genID = params.getGenesisID();
            cp.genHash = new Digest(params.getGenesishashb64());

        } catch (final ApiException e) {
            throw (e);
        }
        return (cp);
    }

    // Utility function for sending a raw signed transaction to the network
    public static TransactionID submitTransaction(final AlgodApi algodApiInstance, final SignedTransaction signedTx)
            throws Exception {
        try {
            // Msgpack encode the signed transaction
            final byte[] encodedTxBytes = Encoder.encodeToMsgPack(signedTx);
            final TransactionID id = algodApiInstance.rawTransaction(encodedTxBytes);
            return (id);
        } catch (final ApiException e) {
            throw (e);
        }
    }

    public static void main(final String args[]) throws Exception {
        // final String ALGOD_API_ADDR = "NODEADDRESS";
        // final String ALGOD_API_TOKEN = "NODETOKEN";
        final String ALGOD_API_ADDR = "http://hackathon.algodev.network:9100";
        final String ALGOD_API_TOKEN = "ef920e2e7e002953f4b29a8af720efe8e4ecc75ff102b165e0472834b25832c1";

        final AlgodClient client = (AlgodClient) new AlgodClient().setBasePath(ALGOD_API_ADDR);
        final ApiKeyAuth api_key = (ApiKeyAuth) client.getAuthentication("api_key");
        api_key.setApiKey(ALGOD_API_TOKEN);
        final AlgodApi algodApiInstance = new AlgodApi(client);

        // Shown for demonstration purposes. NEVER reveal secret mnemonics in practice.
        // These three accounts are for testing purposes
        // Paste in phrase from Step 1A
        // final String account1_mnemonic = "PASTE your phrase for account 1 from Step 1A";
        // final String account2_mnemonic = "PASTE your phrase for account 2 from Step 1A";
        // final String account3_mnemonic = "PASTE your phrase for account 3 from Step 1A";

        final String account1_mnemonic = "portion never forward pill lunch organ biology"
                + " weird catch curve isolate plug innocent skin grunt"
                + " bounce clown mercy hole eagle soul chunk type absorb trim";
        final String account2_mnemonic = "place blouse sad pigeon wing warrior wild script"
                + " problem team blouse camp soldier breeze twist mother"
                + " vanish public glass code arrow execute convince ability" + " there";
        final String account3_mnemonic = "image travel claw climb bottom spot path roast "
                + "century also task cherry address curious save item "
                + "clean theme amateur loyal apart hybrid steak about blanket";

        final Account acct1 = new Account(account1_mnemonic);
        final Account acct2 = new Account(account2_mnemonic);
        final Account acct3 = new Account(account3_mnemonic);
        // get last round and suggested tx fee
        // We use these to get the latest round and tx fees
        // These parameters will be required before every
        // Transaction
        // We will account for changing transaction parameters
        // before every transaction in this example
        ChangingBlockParms cp = null;
        try {
            cp = getChangingParms(algodApiInstance);
        } catch (final ApiException e) {
            e.printStackTrace();
            return;
        }
        System.out.println(acct1.getAddress().toString());
        System.out.println(acct2.getAddress().toString());
        System.out.println(acct3.getAddress().toString());

        // Terminal should look similar to this:

        // THQHGD4HEESOPSJJYYF34MWKOI57HXBX4XR63EPBKCWPOJG5KUPDJ7QJCM
        // AJNNFQN7DSR7QEY766V7JDG35OPM53ZSNF7CU264AWOOUGSZBMLMSKCRIU
        // 3ZQ3SHCYIKSGK7MTZ7PE7S6EDOFWLKDQ6RYYVMT7OHNQ4UJ774LE52AQCU

        // Insert after Step 1 code
        // Create a new asset

        // The following parameters are asset specific
        // and will be re-used throughout the example.

        // Total number of this asset available for circulation
        final BigInteger assetTotal = BigInteger.valueOf(10000);
        // Whether user accounts will need to be unfrozen before transacting
        final boolean defaultFrozen = false;
        // Decimals specifies the number of digits to display after the decimal
        // place when displaying this asset. A value of 0 represents an asset
        // that is not divisible, a value of 1 represents an asset divisible
        // into tenths, and so on. This value must be between 0 and 19
        final Integer assetDecimals = 0;
        // Used to display asset units to user
        final String unitName = "LATINUM";
        // Friendly name of the asset
        final String assetName = "latinum";
        // Optional string pointing to a URL relating to the asset
        final String url = "http://this.test.com";
        // Optional hash commitment of some sort relating to the asset. 32 character
        // length.
        final String assetMetadataHash = "16efaa3924a6fd9d3a4824799a4ac65d";
        // The following parameters are the only ones
        // that can be changed, and they have to be changed
        // by the current manager
        // Specified address can change reserve, freeze, clawback, and manager
        final Address manager = acct2.getAddress();
        // Specified address is considered the asset reserve
        // (it has no special privileges, this is only informational)
        final Address reserve = acct2.getAddress();
        // Specified address can freeze or unfreeze user asset holdings
        final Address freeze = acct2.getAddress();
        // Specified address can revoke user asset holdings and send
        // them to other addresses
        final Address clawback = acct2.getAddress();

        Transaction tx = Transaction.createAssetCreateTransaction(acct1.getAddress(), BigInteger.valueOf(1000),
                cp.firstRound, cp.lastRound, null, cp.genID, cp.genHash, assetTotal, assetDecimals, defaultFrozen,
                unitName, assetName, url, assetMetadataHash.getBytes(), manager, reserve, freeze, clawback);
        // Update the fee as per what the BlockChain is suggesting
        Account.setFeeByFeePerByte(tx, cp.fee);

        // Sign the Transaction
        SignedTransaction signedTx = acct1.signTransaction(tx);
        // send the transaction to the network and
        // wait for the transaction to be confirmed
        BigInteger assetID = null;
        try {
            final TransactionID id = submitTransaction(algodApiInstance, signedTx);
            System.out.println("Transaction ID: " + id);
            waitForTransactionToComplete(algodApiInstance, signedTx.transactionID);
            // Now that the transaction is confirmed we can get the assetID
            final com.algorand.algosdk.algod.client.model.Transaction ptx = algodApiInstance
                    .pendingTransactionInformation(id.getTxId());
            assetID = ptx.getTxresults().getCreatedasset();
        } catch (final Exception e) {
            e.printStackTrace();
            return;
        }
        System.out.println("AssetID = " + assetID);
        AssetParams assetInfo = algodApiInstance.assetInformation(assetID);
        System.out.println(assetInfo);

        // terminal out put should look similar to this:
        // Transaction ID: class TransactionID {
        // txId: HZYZW2FRF6YXJZOJTI2YTDOKNHMTPQR5DBGZMIMGCWXWDC36N3RA
        // }
        // Transaction HZYZW2FRF6YXJZOJTI2YTDOKNHMTPQR5DBGZMIMGCWXWDC36N3RA confirmed in
        // round 4288214
        // AssetID = 150575
        // class AssetParams {
        // assetname: latinum
        // clawbackaddr: AJNNFQN7DSR7QEY766V7JDG35OPM53ZSNF7CU264AWOOUGSZBMLMSKCRIU
        // decimals: 0
        // defaultfrozen: false
        // freezeaddr: AJNNFQN7DSR7QEY766V7JDG35OPM53ZSNF7CU264AWOOUGSZBMLMSKCRIU
        // managerkey: AJNNFQN7DSR7QEY766V7JDG35OPM53ZSNF7CU264AWOOUGSZBMLMSKCRIU
        // metadatahash: [B@41ab013
        // reserveaddr: AJNNFQN7DSR7QEY766V7JDG35OPM53ZSNF7CU264AWOOUGSZBMLMSKCRIU
        // total: 10000
        // unitname: LATINUM
        // url: http://this.test.com
        // }

        // Insert after Step 2's code

        // Change Asset Configuration:
        // Next we will change the asset configuration
        // Note that configuration changes must be done by
        // The manager account, which is currently acct2
        // Note in this transaction we are re-using the asset
        // creation parameters and only changing the manager
        // and transaction parameters like first and last round

        // First we update standard Transaction parameters
        // To account for changes in the state of the blockchain
        try {
            cp = getChangingParms(algodApiInstance);
        } catch (final ApiException e) {
            e.printStackTrace();
            return;
        }

        tx = Transaction.createAssetConfigureTransaction(acct2.getAddress(), BigInteger.valueOf(1000), cp.firstRound,
                cp.lastRound, null, cp.genID, cp.genHash, assetID, acct1.getAddress(), reserve, freeze, clawback,
                false);
        // Update the fee as per what the BlockChain is suggesting
        Account.setFeeByFeePerByte(tx, cp.fee);
        // The transaction must be signed by the current manager account
        // We are reusing the signedTx variable from the first transaction in the
        // example
        signedTx = acct2.signTransaction(tx);
        // send the transaction to the network and
        // wait for the transaction to be confirmed
        try {
            final TransactionID id = submitTransaction(algodApiInstance, signedTx);
            System.out.println("Transaction ID: " + id);
            waitForTransactionToComplete(algodApiInstance, signedTx.transactionID);
        } catch (final Exception e) {
            e.printStackTrace();
            return;
        }

        // Next we will list the newly created asset
        // Get the asset information for the newly changed asset

        assetInfo = algodApiInstance.assetInformation(assetID);
        // The manager should now be the same as the creator
        System.out.println(assetInfo);
        System.out.println("Creator = " + assetInfo.getCreator());

        // terminal outout should look similar to this...
        // Transaction A4NJTLFO5UHCNAAIKYASCJSO2O6V3TNKEEJFQO3S3RY6IKD2F3NQ confirmed in
        // round 4290988
        // class AssetParams {
        // assetname: latinum
        // clawbackaddr: AJNNFQN7DSR7QEY766V7JDG35OPM53ZSNF7CU264AWOOUGSZBMLMSKCRIU
        // decimals: 0
        // defaultfrozen: false
        // freezeaddr: AJNNFQN7DSR7QEY766V7JDG35OPM53ZSNF7CU264AWOOUGSZBMLMSKCRIU
        // managerkey: THQHGD4HEESOPSJJYYF34MWKOI57HXBX4XR63EPBKCWPOJG5KUPDJ7QJCM
        // metadatahash: [B@41ab013
        // reserveaddr: AJNNFQN7DSR7QEY766V7JDG35OPM53ZSNF7CU264AWOOUGSZBMLMSKCRIU
        // total: 10000
        // unitname: LATINUM
        // url: http://this.test.com
        // }
        // Creator = THQHGD4HEESOPSJJYYF34MWKOI57HXBX4XR63EPBKCWPOJG5KUPDJ7QJCM

        // insert after code from Step 3's code

        // Opt-in to Receive Asset
        // All accounts that want recieve the new asset have to opt in.
        // To do this they send an asset transfer of the new asset to themseleves with
        // an ammount of 0
        //
        // In this example we are setting up the 3rd recovered account to
        // receive the new asset
        // First we update standard Transaction parameters
        // To account for changes in the state of the blockchain
        // com.algorand.algosdk.algod.client.model.Account act = new
        // com.algorand.algosdk.algod.client.model.Account();
        try {
            cp = getChangingParms(algodApiInstance);
        } catch (final ApiException e) {
            e.printStackTrace();
            return;
        }
        tx = Transaction.createAssetAcceptTransaction(acct3.getAddress(), BigInteger.valueOf(1000), cp.firstRound,
                cp.lastRound, null, cp.genID, cp.genHash, assetID);
        // Update the fee based on the network suggested fee
        Account.setFeeByFeePerByte(tx, cp.fee);
        // The transaction must be signed by the current manager account
        // We are reusing the signedTx variable from the first transaction in the
        // example
        signedTx = acct3.signTransaction(tx);
        // send the transaction to the network and
        // wait for the transaction to be confirmed
        com.algorand.algosdk.algod.client.model.Account act = algodApiInstance
                .accountInformation(acct3.getAddress().toString());
        try {
            final TransactionID id = submitTransaction(algodApiInstance, signedTx);
            System.out.println("Transaction ID: " + id);
            waitForTransactionToComplete(algodApiInstance, signedTx.transactionID);
            // We can now list the account information for acct3
            // and see that it can accept the new asseet
            assetInfo = algodApiInstance.assetInformation(assetID);
            System.out.println("AssetID: " + assetID);
            System.out.println("Creator = " + assetInfo.getCreator());

            act = algodApiInstance.accountInformation(acct3.getAddress().toString());
            System.out.println("Account opt in for Asset transfer " + acct3.getAddress().toString());
            System.out.println("Account Asset Holding Amount = " + act.getHolding(assetID).getAmount());

        } catch (final Exception e) {
            e.printStackTrace();
            return;
        }
        // terminal output should be similar to this...

        // Transaction BQYA3R7L3M63VGLQBTWZURLWSBISM62KFRUGCK2RGBTQTBNNULUA confirmed in
        // round 4296935
        // AssetID: 151126
        // Creator = THQHGD4HEESOPSJJYYF34MWKOI57HXBX4XR63EPBKCWPOJG5KUPDJ7QJCM
        // Account opt in for Asset transfer
        // 3ZQ3SHCYIKSGK7MTZ7PE7S6EDOFWLKDQ6RYYVMT7OHNQ4UJ774LE52AQCU
        // Account Asset Holding Amount = 0

        // Insert code after Step 4's code

        // Transfer the Asset:
        // Now that account3 can recieve the new asset we can tranfer assets
        // from the creator (account 1) to account3

        // First we update standard Transaction parameters
        // To account for changes in the state of the blockchain
        try {
            cp = getChangingParms(algodApiInstance);
        } catch (ApiException e) {
            e.printStackTrace();
            return;
        }
        // Next we set asset xfer specific parameters
        // We set the assetCloseTo to null so we do not close the asset out
        Address assetCloseTo = new Address();
        BigInteger assetAmount = BigInteger.valueOf(10);
        tx = Transaction.createAssetTransferTransaction(acct1.getAddress(), acct3.getAddress(), assetCloseTo,
                assetAmount, BigInteger.valueOf(1000), cp.firstRound, cp.lastRound, null, cp.genID, cp.genHash,
                assetID);
        // Update the fee based on the network suggested fee
        Account.setFeeByFeePerByte(tx, cp.fee);
        // The transaction must be signed by the sender account
        // We are reusing the signedTx variable from the first transaction in the
        // example
        signedTx = acct1.signTransaction(tx);
        // send the transaction to the network and
        // wait for the transaction to be confirmed
        try {
            TransactionID id = submitTransaction(algodApiInstance, signedTx);
            System.out.println("Transaction ID: " + id);
            waitForTransactionToComplete(algodApiInstance, signedTx.transactionID);
            // We can now list the account information for acct3
            // and see that it now has 10 of the new asset
            assetInfo = algodApiInstance.assetInformation(assetID);
            System.out.println("AssetID: " + assetID);
            System.out.println("Creator = " + assetInfo.getCreator());

            act = algodApiInstance.accountInformation(acct3.getAddress().toString());
            System.out.println("Account w Asset transfered to " + acct3.getAddress().toString());
            System.out.println("Account Asset Holding Amount = " + act.getHolding(assetID).getAmount());

        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // termianl output should look similar to this
        // Transaction HOYHCDQD3ZMHW5TOA2NBSDGVB4GPRQ26R6AQLNZPC64QJKL5RUHA confirmed in
        // round 4296943
        // AssetID: 151126
        // Creator = THQHGD4HEESOPSJJYYF34MWKOI57HXBX4XR63EPBKCWPOJG5KUPDJ7QJCM
        // Account w Asset transfered to
        // 3ZQ3SHCYIKSGK7MTZ7PE7S6EDOFWLKDQ6RYYVMT7OHNQ4UJ774LE52AQCU
        // Account Asset Holding Amount = 10

        // Insert after Step 5's code
        // Java
        // Freeze the Asset:
        // The asset was created and configured to allow freezing an account
        // If the freeze address is blank, it will no longer be possible to do this.
        // In this example we will now freeze account3 from transacting with the
        // The newly created asset.
        // The freeze transaction is sent from the freeze acount
        // Which in this example is account2
        // First we update standard Transaction parameters
        // To account for changes in the state of the blockchain
        try {
            cp = getChangingParms(algodApiInstance);
        } catch (ApiException e) {
            e.printStackTrace();
            return;
        }
        // Next we set asset xfer specific parameters
        boolean freezeState = true;
        // The sender should be freeze account acct2
        // Theaccount to freeze should be set to acct3
        tx = Transaction.createAssetFreezeTransaction(acct2.getAddress(), acct3.getAddress(), freezeState,
                BigInteger.valueOf(1000), cp.firstRound, cp.lastRound, null, cp.genHash, assetID);
        // Update the fee based on the network suggested fee
        Account.setFeeByFeePerByte(tx, cp.fee);
        // The transaction must be signed by the freeze account acct2
        // We are reusing the signedTx variable from the first transaction in the
        // example
        signedTx = acct2.signTransaction(tx);
        // send the transaction to the network and
        // wait for the transaction to be confirmed
        try {
            TransactionID id = submitTransaction(algodApiInstance, signedTx);
            System.out.println("Transaction ID: " + id);
            waitForTransactionToComplete(algodApiInstance, signedTx.transactionID);
            // We can now list the account information for acct3
            // and see that it now frozen
            // Note--currently no getter method for frozen state
            act = algodApiInstance.accountInformation(acct3.getAddress().toString());
            System.out.println(act.getHolding(assetID).toString());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        assetInfo = algodApiInstance.assetInformation(assetID);
        System.out.println("AssetID: " + assetID);
        System.out.println("Creator = " + assetInfo.getCreator());

        act = algodApiInstance.accountInformation(acct3.getAddress().toString());
        System.out.println("Account w Asset frozen " + acct3.getAddress().toString());
        System.out.println("Account Asset Holding Info = " + act);

        // termianl out put should show something similar to this
        // AssetID: 151234
        // Creator = THQHGD4HEESOPSJJYYF34MWKOI57HXBX4XR63EPBKCWPOJG5KUPDJ7QJCM
        // Account w Asset frozen
        // 3ZQ3SHCYIKSGK7MTZ7PE7S6EDOFWLKDQ6RYYVMT7OHNQ4UJ774LE52AQCU

        // Insert after Step 6's code

        // Revoke the asset:
        // The asset was also created with the ability for it to be revoked by
        // clawbackaddress. If the asset was created or configured by the manager
        // not allow this by setting the clawbackaddress to a blank address
        // then this would not be possible.
        // We will now clawback the 10 assets in account3. Account2
        // is the clawbackaccount and must sign the transaction
        // The sender will be be the clawback adress.
        // the recipient will also be be the creator acct1 in this case
        // First we update standard Transaction parameters
        // To account for changes in the state of the blockchain
        try {
            cp = getChangingParms(algodApiInstance);
        } catch (ApiException e) {
            e.printStackTrace();
            return;
        }
        // Next we set asset xfer specific parameters
        assetAmount = BigInteger.valueOf(10);
        tx = Transaction.createAssetRevokeTransaction(acct2.getAddress(), acct3.getAddress(), acct1.getAddress(),
                assetAmount, BigInteger.valueOf(1000), cp.firstRound, cp.lastRound, null, cp.genID, cp.genHash,
                assetID);
        // Update the fee based on the network suggested fee
        Account.setFeeByFeePerByte(tx, cp.fee);
        // The transaction must be signed by the clawback account
        // We are reusing the signedTx variable from the first transaction in the
        // example
        signedTx = acct2.signTransaction(tx);
        // send the transaction to the network and
        // wait for the transaction to be confirmed
        try {
            TransactionID id = submitTransaction(algodApiInstance, signedTx);
            System.out.println("Transaction ID: " + id);
            waitForTransactionToComplete(algodApiInstance, signedTx.transactionID);
            // We can now list the account information for acct3
            // and see that it now has 0 of the new asset
            act = algodApiInstance.accountInformation(acct3.getAddress().toString());
            System.out.println("Account 3 Asset amount = " + act.getHolding(assetID).getAmount());
            act = algodApiInstance.accountInformation(acct1.getAddress().toString());
            System.out.println("Account 1 Asset amount = " + act.getHolding(assetID).getAmount());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // your terminal output should look something like this
        // Transaction 6ZC6DBYAY4DI7HFZPDHRINXZQNDZX3WHHTM6OOYLPJ6REHUSRYOQ confirmed in
        // round 4298849
        // Account 3 Asset amount = 0
        // Account 1 Asset amount = 10000

        // Insert after Step 7's code

        // Destroy the Asset:
        // All of the created assets should now be back in the creators
        // Account so we can delete the asset.
        // If this is not the case the asset deletion will fail
        // The address for the from field must be the creator
        // First we update standard Transaction parameters
        // To account for changes in the state of the blockchain
        try {
            cp = getChangingParms(algodApiInstance);
        } catch (ApiException e) {
            e.printStackTrace();
            return;
        }
        // Next we set asset xfer specific parameters
        // The manager must sign and submit the transaction
        // This is currently set to acct1
        tx = Transaction.createAssetDestroyTransaction(acct1.getAddress(), BigInteger.valueOf(1000), cp.firstRound,
                cp.lastRound, null, cp.genHash, assetID);
        // Update the fee based on the network suggested fee
        Account.setFeeByFeePerByte(tx, cp.fee);
        // The transaction must be signed by the manager account
        // We are reusing the signedTx variable from the first transaction in the
        // example
        signedTx = acct1.signTransaction(tx);
        // send the transaction to the network and
        // wait for the transaction to be confirmed
        try {
            TransactionID id = submitTransaction(algodApiInstance, signedTx);
            System.out.println("Transaction ID: " + id);
            waitForTransactionToComplete(algodApiInstance, signedTx.transactionID);
            // We can now list the account information for acct1
            // and see that the asset is no longer there
            act = algodApiInstance.accountInformation(acct1.getAddress().toString());
            System.out.println("Does AssetID: " + assetID + " exist? " + act.getThisassettotal().containsKey(assetID));
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        // your terminal output should look similar to this
        // Transaction SV5UPKAG7CI3BUZ74SL5VDFHRS2SGUVKAJVKNHSUZWIPM6HSRFEA confirmed in
        // round 4299035
        // Does AssetID: 151265 exist? false
    }

}