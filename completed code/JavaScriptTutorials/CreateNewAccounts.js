const algosdk = require('algosdk');

// In order to do this ASA tutorial, we will need to generate 3 accounts
// once created copy off the values which we will past into the TutorialASA code
// once created sucessfully, you will need to add funds to all three
// The Algorand TestNet Dispenser is located here: 
// https://bank.testnet.algorand.network/

// Retrieve the token, server and port values for your installation in the algod.net
// and algod.token files within the data directory
// or use PureStake API service https://www.purestake.com/

// UPDATE THESE VALUES
// const token = "TOKEN";
// const server = "SERVER";
// const port = PORT;

const token = "ef920e2e7e002953f4b29a8af720efe8e4ecc75ff102b165e0472834b25832c1";
const server = "http://hackathon.algodev.network";
const port = 9100;

var acct = null;

acct = algosdk.generateAccount();

account1 = acct.addr;
console.log("Account 1 = " + account1);
var account1_mnemonic = algosdk.secretKeyToMnemonic(acct.sk);
console.log("Account Mnemonic 1 = "+ account1_mnemonic);
var recoveredAccount1 = algosdk.mnemonicToSecretKey(account1_mnemonic);
var isValid = algosdk.isValidAddress(recoveredAccount1.addr);
console.log("Is this a valid address: " + isValid);
console.log("Account created. Save off Mnemonic and address");

acct = algosdk.generateAccount();

account2 = acct.addr;
console.log("Account 2 = " + account2);
var account2_mnemonic = algosdk.secretKeyToMnemonic(acct.sk);
console.log("Account Mnemonic 2 = " +account2_mnemonic);
var recoveredAccount2 = algosdk.mnemonicToSecretKey(account2_mnemonic);
var isValid = algosdk.isValidAddress(recoveredAccount2.addr);
console.log("Is this a valid address: " + isValid);
console.log("Account created. Save off Mnemonic and address");

acct = algosdk.generateAccount();

account3 = acct.addr;
console.log("Account 3 = " + account3);
var account3_mnemonic = algosdk.secretKeyToMnemonic(acct.sk);
console.log("Account Mnemonic 3 = " +account3_mnemonic);
var recoveredAccount3 = algosdk.mnemonicToSecretKey(account3_mnemonic);
var isValid = algosdk.isValidAddress(recoveredAccount3.addr);
console.log("Is this a valid address: " + isValid);
console.log("Account created. Save off Mnemonic and address");
console.log("");
console.log("Add funds to all of these accounts using the TestNet Dispenser at https://bank.testnet.algorand.network/ ");
console.log("");
console.log("Copy off these 3 lines of code and they will be pasted in the ASA Tutorial code");
console.log("");
console.log("var account1_mnemonic = \"" + account1_mnemonic + "\"");
console.log("var account2_mnemonic = \"" + account2_mnemonic + "\"");
console.log("var account3_mnemonic = \"" + account3_mnemonic + "\"");


//
// your terminal output should look similar to this:

// Account 1 = GNZLAV7QIYGISQ4EYY4Q2LUKHBXKXKPVMQJDWLZXDY57Z46HATOFMBWP7A
// Account Mnemonic 1 = hidden glove heart now claw away business behind echo best select merry secret upset shed margin outer reflect holiday rib among script scorpion above oval
// Is this a valid address: true
// Account created.Save off Mnemonic and address

// Account 2 = 4N6OZZU73MUIS7IWW2OVSDXRIK6JSI5QAOWHU2CZGIVF4WNQUYFOMY7UEA
// Account Mnemonic 2 = plug cabin shuffle hope similar exact enable speed amateur shift ramp machine scrap comfort shove carry test marble hip protect melody win wink ability hybrid
// Is this a valid address: true
// Account created.Save off Mnemonic and address

// Account 3 = LCNNXW27VDCFYZI3AERPBHJ3DWIC3PJ4DQYDQBFHWR4UNDYFZVYT2VJ5LU
// Account Mnemonic 3 = divert feel ball make absorb deny student turn fuel north slice wolf small tribe pull work scout empty nose riot zoo prepare loan absent kingdom
// Is this a valid address: true
// Account created.Save off Mnemonic and address

// Add funds to all of these accounts using the TestNet Dispenser at https://bank.testnet.algorand.network/ 

// Copy off these 3 lines of code and they will be pasted in the ASA Tutorial code

// var account1_mnemonic = "hidden glove heart now claw away business behind echo best select merry secret upset shed margin outer reflect holiday rib among script scorpion above oval"
// var account2_mnemonic = "plug cabin shuffle hope similar exact enable speed amateur shift ramp machine scrap comfort shove carry test marble hip protect melody win wink ability hybrid"
// var account3_mnemonic = "divert feel ball make absorb deny student turn fuel north slice wolf small tribe pull work scout empty nose riot zoo prepare loan absent kingdom"