package main

import (
	// b64 "encoding/base64"
	json "encoding/json"
	"fmt"

	"github.com/algorand/go-algorand-sdk/client/algod"
	// "github.com/algorand/go-algorand-sdk/crypto"
	"github.com/algorand/go-algorand-sdk/mnemonic"
	// "github.com/algorand/go-algorand-sdk/transaction"
)

// UPDATE THESE VALUES
// const algodAddress = "your ADDRESS"
// const algodToken = "your TOKEN"

const algodAddress = "http://localhost:4001"
const algodToken = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"

// Paste in mnemonic phrases for all three accounts
const mnemonic1 = "buzz genre work meat fame favorite rookie stay tennis demand panic busy hedgehog snow morning acquire ball grain grape member blur armor foil ability seminar"
const mnemonic2 = "design country rebuild myth square resemble flock file whisper grunt hybrid floor letter pet pull hurry choice erase heart spare seven idea multiply absent seven"
const mnemonic3 = "news slide thing empower naive same belt evolve lawn ski chapter melody weasel supreme abuse main olive sudden local chat candy daughter hand able drip"

var txHeaders = append([]*algod.Header{}, &algod.Header{"Content-Type", "application/json"})

// Accounts to be used through examples
func loadAccounts() (map[int][]byte, map[int]string) {
	// Shown for demonstration purposes. NEVER reveal secret mnemonics in practice.
	// Change these values to use your accounts.

	var pks = map[int]string{
		1: "THQHGD4HEESOPSJJYYF34MWKOI57HXBX4XR63EPBKCWPOJG5KUPDJ7QJCM",
		2: "AJNNFQN7DSR7QEY766V7JDG35OPM53ZSNF7CU264AWOOUGSZBMLMSKCRIU",
		3: "3ZQ3SHCYIKSGK7MTZ7PE7S6EDOFWLKDQ6RYYVMT7OHNQ4UJ774LE52AQCU",
	}

	mnemonics := []string{mnemonic1, mnemonic2, mnemonic3}
	var sks = make(map[int][]byte)
	for i, m := range mnemonics {
		var err error
		sks[i+1], err = mnemonic.ToPrivateKey(m)
		if err != nil {
			fmt.Printf("Issue with account %d private key conversion.", i+1)
		} else {
			fmt.Printf("Loaded Key %d: %s\n", i+1, pks[i+1])
		}
	}
	return sks, pks
}

// Function that waits for a given txId to be confirmed by the network
func waitForConfirmation(algodClient algod.Client, txId string) {
	for {
		b3, err := algodClient.PendingTransactionInformation(txId, txHeaders...)
		if err != nil {
			fmt.Printf("waiting for confirmation... (pool error, if any): %s\n", err)
			continue
		}
		if b3.ConfirmedRound > 0 {
			fmt.Printf("Transaction "+b3.TxID+" confirmed in round %d\n",
				b3.ConfirmedRound)
			break
		}
	}
}

// PrettyPrint Go structs
func PrettyPrint(data interface{}) {
	var p []byte
	// var err := error
	p, err := json.MarshalIndent(data, "", "\t")
	if err != nil {
		fmt.Println(err)
		return
	}
	fmt.Printf("%s \n", p)
}

// Main function to demonstrate ASA examples

func main() {
	// Get pre-defined set of keys for example
	sks, pks := loadAccounts()

	// Initialize an algodClient
	algodClient, err := algod.MakeClient(algodAddress, algodToken)
	if err != nil {
		return
	}
	// Get network-related transaction parameters and assign
	txParams, err := algodClient.SuggestedParams()
	if err != nil {
		fmt.Printf("error getting suggested tx params: %s\n", err)
		return
	}

	// Print info
	mnemonics := []string{mnemonic1, mnemonic2, mnemonic3}
	PrettyPrint(mnemonics)
	PrettyPrint(txParams)
	PrettyPrint(pks)
	PrettyPrint(sks)
}
