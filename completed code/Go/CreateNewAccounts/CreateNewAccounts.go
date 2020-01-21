package main

import (
	"fmt"

	"github.com/algorand/go-algorand-sdk/crypto"
	"github.com/algorand/go-algorand-sdk/mnemonic"
)

func main() {
	account1 := crypto.GenerateAccount()
	account2 := crypto.GenerateAccount()
	account3 := crypto.GenerateAccount()
	address1 := account1.Address.String()
	address2 := account2.Address.String()
	address3 := account3.Address.String()

	mnemonic1, err := mnemonic.FromPrivateKey(account1.PrivateKey)
	if err != nil {
		return
	}
	mnemonic2, err := mnemonic.FromPrivateKey(account2.PrivateKey)
	if err != nil {
		return
	}
	mnemonic3, err := mnemonic.FromPrivateKey(account3.PrivateKey)
	if err != nil {
		return
	}
	fmt.Printf("1 : \"%s\"\n", address1)
	fmt.Printf("2 : \"%s\"\n", address2)
	fmt.Printf("3 : \"%s\"\n", address3)
	fmt.Printf("")
	fmt.Printf("Copy off accounts above and add TestNet Algo funds using the TestNet Dispenser at https://bank.testnet.algorand.network/\n")
	fmt.Printf("Copy off the following mnemonic code for use in Step 1B\n")
	fmt.Printf("\n")
	fmt.Printf("mnemonic1 := \"%s\"\n", mnemonic1)
	fmt.Printf("mnemonic2 := \"%s\"\n", mnemonic2)
	fmt.Printf("mnemonic3 := \"%s\"\n", mnemonic3)
}

// Terminal output should look similar to this...

// 1 : "RAWIR4W53EX37HGN2FKLFQK53FF3NM6ASREKABFK6UOWMS3JYSMEVOMOVM"
// 2 : "RPL3RD7JFH73ZXMK6B42UPBU2F77VYS5ROQBCL7YYXBY5SZKG72OL5WVCY"
// 3 : "J6FRXRPBSUIDEICILWW53ZSATASGRHUCWDIVEN3MGPU7KUKEGJNQ4MRNMM"
// Copy off accounts above and add TestNet Algo funds using the TestNet Dispenser at https://bank.testnet.algorand.network/
// Copy off the following mnemonic code for use in Step 1B

// mnemonic1 := "donkey remember meadow box unfold firm relief wonder that shoe tonight alcohol visit where kingdom captain fitness plate lend leopard wear avocado science absent park"
// mnemonic2 := "trial stem exclude else turn ankle egg dish mountain major link confirm inherit crouch conduct below faint valid journey boat route pizza recycle above leopard"
// mnemonic3 := "relief enough stadium diagram rubber please summer local math flush adapt belt void bitter roast amazing dove life wish obtain toward merry unlock able help"