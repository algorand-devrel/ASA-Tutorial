package com.algorand.Tutorials;

import com.algorand.algosdk.account.Account;
import com.algorand.algosdk.crypto.Address;

public class CreateNewAccounts {

    public static void main(final String args[]) throws Exception {
        // Create a random new account
        Account act1 = new Account();
        // Get the new account address
        Address addr1 = act1.getAddress();
        // Get the backup phrase
        String backup1 = act1.toMnemonic();
        System.out.println("Account Address 1: " + addr1.toString());
        // Create a random new account
        Account act2 = new Account();
        // Get the new account address
        Address addr2 = act2.getAddress();
        // Get the backup phrase
        String backup2 = act2.toMnemonic();
        System.out.println("Account Address 2: " + addr2.toString());
        // Create a random new account
        Account act3 = new Account();
        // Get the new account address
        Address addr3 = act3.getAddress();
        // Get the backup phrase
        String backup3 = act3.toMnemonic();
        System.out.println("Account Address 3: " + addr3.toString());
        System.out.println("");
        System.out.println(
                "Copy off accounts above and add TestNet Algo funds using the TestNet Dispenser at https://bank.testnet.algorand.network/");
        System.out.println("");
        System.out.println("copy off the following code for use in Step 1B");
        System.out.println("");
        System.out.println("final String account1_mnemonic = \"" + backup1 + "\"");
        System.out.println("final String account2_mnemonic = \"" + backup2 + "\"");
        System.out.println("final String account3_mnemonic = \"" + backup3 + "\"");
        // Terminal output should look similar to this...

        // Account Address 1: PSF2S7OTAIP6YEQBOF4R7JOYDBZMTMAG7KCAUCQUM6UPCELHNRS5LQPS3M
        // Account Address 2: FIBG65O25CMDTEG5ARZBDWABUDO3Z6ZRVNTAJN4DCLYCI6Z4YZFSEWF55M
        // Account Address 3: AWICWFUDL5CO3XYSWGRMXLS7ML2RGOSCWQNXNSFVR2VOP73P6IMEZ7FUFY

        // Copy off accounts above and add TestNet Algo funds using the TestNet
        // Dispenser at https://bank.testnet.algorand.network/

        // copy off the following code for use in Step 1B

        // final String account1_mnemonic = "tongue mention field lottery diagram yellow
        // walk erupt supply link bike setup learn excite recycle lunar flag dash lucky
        // dad evoke refuse ticket able please"
        // final String account2_mnemonic = "over fantasy smile pupil visual film club
        // balcony jacket tilt amateur whip crisp allow clay govern canoe fat group
        // develop sorry test unknown absent dinner"
        // final String account3_mnemonic = "category sick rough intact design board
        // episode hedgehog build bundle siege voyage rent season fossil weird cannon
        // pipe grow winner wall urge report about weapon"

    }

}
