package ovh.sad.jkromer;

import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.http.addresses.*;
import ovh.sad.jkromer.http.internal.CreateWallet;
import ovh.sad.jkromer.http.internal.GiveMoney;
import ovh.sad.jkromer.http.misc.GetMotd;
import ovh.sad.jkromer.http.misc.GetSupply;
import ovh.sad.jkromer.http.misc.StartWs;
import ovh.sad.jkromer.http.names.*;
import ovh.sad.jkromer.http.transactions.GetTransaction;
import ovh.sad.jkromer.http.transactions.ListLatestTransactions;
import ovh.sad.jkromer.http.transactions.ListTransactions;
import ovh.sad.jkromer.http.v1.GetPlayerByName;
import ovh.sad.jkromer.http.v1.GetPlayerByUuid;

import java.util.UUID;

public class Tests {

    public static String kromerKey = "..";
    public static String validAddress = "kmmx7kcr4o";
    public static String availableName = "cock2";
    public static String unavailableName = "cock";
    public static String transactionId = "6";

    public static class Addresses {
        static void GetAddress() {
            Result<GetAddress.GetAddressBody> result = GetAddress.execute(validAddress).join();
            if (result instanceof Result.Ok) {
                Result.Ok<GetAddress.GetAddressBody> ok = (Result.Ok<GetAddress.GetAddressBody>) result;
                GetAddress.GetAddressBody value = ok.value();
                System.out.println("Address: " + value.address.balance);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<GetAddress.GetAddressBody> err = (Result.Err<GetAddress.GetAddressBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void ListAddresses() {
            Result<ListAddresses.ListAddressesBody> result = ListAddresses.execute().join();
            if (result instanceof Result.Ok) {
                Result.Ok<ListAddresses.ListAddressesBody> ok = (Result.Ok<ListAddresses.ListAddressesBody>) result;
                ListAddresses.ListAddressesBody value = ok.value();
                System.out.println("Address: " + value.addresses.get(0).address);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<ListAddresses.ListAddressesBody> err = (Result.Err<ListAddresses.ListAddressesBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void RichAddresses() {
            Result<RichAddresses.RichAddressesBody> result = RichAddresses.execute().join();
            if (result instanceof Result.Ok) {
                Result.Ok<RichAddresses.RichAddressesBody> ok = (Result.Ok<RichAddresses.RichAddressesBody>) result;
                RichAddresses.RichAddressesBody value = ok.value();
                System.out.println("Address: " + value.addresses.get(0).address + ", balance: " + value.addresses.get(0).balance + "KRO! Wow.");
            } else if (result instanceof Result.Err<?>) {
                Result.Err<RichAddresses.RichAddressesBody> err = (Result.Err<RichAddresses.RichAddressesBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void GetAddressTransactions() {
            Result<GetAddressTransactions.GetAddressTransactionsBody> result = GetAddressTransactions.execute(validAddress).join();
            if (result instanceof Result.Ok) {
                Result.Ok<GetAddressTransactions.GetAddressTransactionsBody> ok = (Result.Ok<GetAddressTransactions.GetAddressTransactionsBody>) result;
                GetAddressTransactions.GetAddressTransactionsBody value = ok.value();
                System.out.println("From: " + value.transactions.get(0).from
                        + ", to: " + value.transactions.get(0).to + ", of " + value.transactions.get(0).value +"KRO! Wow.");
            } else if (result instanceof Result.Err<?>) {
                Result.Err<GetAddressTransactions.GetAddressTransactionsBody> err = (Result.Err<GetAddressTransactions.GetAddressTransactionsBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void GetAddressNames() {
            Result<GetAddressNames.GetAddressNamesBody> result = GetAddressNames.execute(validAddress).join();
            if (result instanceof Result.Ok) {
                Result.Ok<GetAddressNames.GetAddressNamesBody> ok = (Result.Ok<GetAddressNames.GetAddressNamesBody>) result;
                GetAddressNames.GetAddressNamesBody value = ok.value();
                System.out.println("Current owner: " + value.names.get(0).owner
                        + ", Original owner: " + value.names.get(0).original_owner + ", A record: " + value.names.get(0).a);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<GetAddressNames.GetAddressNamesBody> err = (Result.Err<GetAddressNames.GetAddressNamesBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }
    }

    public static class Misc {
        static void GetMotd() {
            Result<GetMotd.GetMotdBody> result = GetMotd.execute().join();
            if (result instanceof Result.Ok) {
                Result.Ok<GetMotd.GetMotdBody> ok = (Result.Ok<GetMotd.GetMotdBody>) result;
                GetMotd.GetMotdBody value = ok.value();
                System.out.println("Version " + value.motdPackage.version + " of " + value.motdPackage.name + " downloadable at " + value.motdPackage.repository + ".");
            } else if (result instanceof Result.Err<?>) {
                Result.Err<GetMotd.GetMotdBody> err = (Result.Err<GetMotd.GetMotdBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void Login() {
            System.out.println("A test for Login is not implemented.");
        }

        static void StartWs() {
            Result<StartWs.StartWsResponse> result = StartWs.execute().join();
            if (result instanceof Result.Ok) {
                Result.Ok<StartWs.StartWsResponse> ok = (Result.Ok<StartWs.StartWsResponse>) result;
                StartWs.StartWsResponse value = ok.value();
                System.out.println("URL: " + value.url);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<StartWs.StartWsResponse> err = (Result.Err<StartWs.StartWsResponse>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void GetSupply() {
            Result<GetSupply.GetSupplyBody> result = GetSupply.execute().join();
            if (result instanceof Result.Ok) {
                Result.Ok<GetSupply.GetSupplyBody> ok = (Result.Ok<GetSupply.GetSupplyBody>) result;
                GetSupply.GetSupplyBody value = ok.value();
                System.out.println("Supply: " + value.money_supply);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<GetSupply.GetSupplyBody> err = (Result.Err<GetSupply.GetSupplyBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }
    }

    public static class Internal {
        static void CreateWallet() {
            Result<CreateWallet.CreateWalletResponse> result = CreateWallet.execute(kromerKey, "hello", UUID.randomUUID().toString()).join();
            if (result instanceof Result.Ok) {
                Result.Ok<CreateWallet.CreateWalletResponse> ok = (Result.Ok<CreateWallet.CreateWalletResponse>) result;
                CreateWallet.CreateWalletResponse value = ok.value();
                System.out.println(value.privatekey);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<CreateWallet.CreateWalletResponse> err = (Result.Err<CreateWallet.CreateWalletResponse>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void GiveMoney() {
            Result<GiveMoney.GiveMoneyResponse> result = GiveMoney.execute(kromerKey, 100, validAddress).join();
            if (result instanceof Result.Ok) {
                Result.Ok<GiveMoney.GiveMoneyResponse> ok = (Result.Ok<GiveMoney.GiveMoneyResponse>) result;
                GiveMoney.GiveMoneyResponse value = ok.value();
                System.out.println(value.private_key + " + 100");
            } else if (result instanceof Result.Err<?>) {
                Result.Err<GiveMoney.GiveMoneyResponse> err = (Result.Err<GiveMoney.GiveMoneyResponse>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }
    }

    public static class Names {
        static void GetName() {
            Result<GetName.GetNameBody> result = GetName.execute(unavailableName).join();
            if (result instanceof Result.Ok) {
                Result.Ok<GetName.GetNameBody> ok = (Result.Ok<GetName.GetNameBody>) result;
                GetName.GetNameBody value = ok.value();
                System.out.println("Owner: " + value.name.owner);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<GetName.GetNameBody> err = (Result.Err<GetName.GetNameBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void ListNames() {
            Result<ListNames.ListNamesBody> result = ListNames.execute().join();
            if (result instanceof Result.Ok) {
                Result.Ok<ListNames.ListNamesBody> ok = (Result.Ok<ListNames.ListNamesBody>) result;
                ListNames.ListNamesBody value = ok.value();
                System.out.println("Owner: " + value.names.get(0).owner);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<ListNames.ListNamesBody> err = (Result.Err<ListNames.ListNamesBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void ListNewestNames() {
            Result<ListNewestNames.ListNewestNamesBody> result = ListNewestNames.execute().join();
            if (result instanceof Result.Ok) {
                Result.Ok<ListNewestNames.ListNewestNamesBody> ok = (Result.Ok<ListNewestNames.ListNewestNamesBody>) result;
                ListNewestNames.ListNewestNamesBody value = ok.value();
                System.out.println("Owner: " + value.names.get(0).owner);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<ListNewestNames.ListNewestNamesBody> err = (Result.Err<ListNewestNames.ListNewestNamesBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void GetNameCost() {
            Result<GetNameCost.GetNameCostBody> result = GetNameCost.execute().join();
            if (result instanceof Result.Ok) {
                Result.Ok<GetNameCost.GetNameCostBody> ok = (Result.Ok<GetNameCost.GetNameCostBody>) result;
                GetNameCost.GetNameCostBody value = ok.value();
                System.out.println("Cost: " + value.name_cost);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<GetNameCost.GetNameCostBody> err = (Result.Err<GetNameCost.GetNameCostBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void GetNameAvailability() {
            Result<GetNameAvailability.GetNameAvailabilityBody> unavailable = GetNameAvailability.execute(unavailableName).join();
            if (unavailable instanceof Result.Ok) {
                Result.Ok<GetNameAvailability.GetNameAvailabilityBody> ok = (Result.Ok<GetNameAvailability.GetNameAvailabilityBody>) unavailable;
                GetNameAvailability.GetNameAvailabilityBody value = ok.value();
                System.out.println("Availability: " + value.available + " (should be false)");
            } else if (unavailable instanceof Result.Err<?>) {
                Result.Err<GetNameAvailability.GetNameAvailabilityBody> err = (Result.Err<GetNameAvailability.GetNameAvailabilityBody>) unavailable;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }

            Result<GetNameAvailability.GetNameAvailabilityBody> available = GetNameAvailability.execute(availableName).join();
            if (available instanceof Result.Ok) {
                Result.Ok<GetNameAvailability.GetNameAvailabilityBody> ok = (Result.Ok<GetNameAvailability.GetNameAvailabilityBody>) available;
                GetNameAvailability.GetNameAvailabilityBody value = ok.value();
                System.out.println("Availability: " + value.available + " (should be true)");
            } else if (available instanceof Result.Err<?>) {
                Result.Err<GetNameAvailability.GetNameAvailabilityBody> err = (Result.Err<GetNameAvailability.GetNameAvailabilityBody>) available;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void RegisterName() {
            System.out.println("A test for RegisterName is not implemented.");
        }

        static void UpdateName() {
            System.out.println("A test for UpdateName is not implemented.");
        }
    }

    public static class V1 {
        static void GetPlayerByName() {
            Result<GetPlayerByName.GetPlayerByResponse> result = GetPlayerByName.execute("hartbreix").join();
            if (result instanceof Result.Ok) {
                Result.Ok<GetPlayerByName.GetPlayerByResponse> ok = (Result.Ok<GetPlayerByName.GetPlayerByResponse>) result;
                GetPlayerByName.GetPlayerByResponse value = ok.value();
                System.out.println("address: " + value.data.get(0).address + ", balance: " + value.data.get(0).balance);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<GetPlayerByName.GetPlayerByResponse> err = (Result.Err<GetPlayerByName.GetPlayerByResponse>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void GetPlayerByUuid() {
            Result<GetPlayerByName.GetPlayerByResponse> result = GetPlayerByUuid.execute("21f7143a-45cd-4995-b1e3-6c3c8602ef7c").join();
            if (result instanceof Result.Ok) {
                Result.Ok<GetPlayerByName.GetPlayerByResponse> ok = (Result.Ok<GetPlayerByName.GetPlayerByResponse>) result;
                GetPlayerByName.GetPlayerByResponse value = ok.value();
                System.out.println("address: " + value.data.get(0).address + ", balance: " + value.data.get(0).balance);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<GetPlayerByName.GetPlayerByResponse> err = (Result.Err<GetPlayerByName.GetPlayerByResponse>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }
    }

    public static class Transactions {
        static void ListTransaction() {
            Result<ListTransactions.ListTransactionsBody> result = ListTransactions.execute().join();
            if (result instanceof Result.Ok) {
                Result.Ok<ListTransactions.ListTransactionsBody> ok = (Result.Ok<ListTransactions.ListTransactionsBody>) result;
                ListTransactions.ListTransactionsBody value = ok.value();
                System.out.println("From: " + value.transactions.get(0).from
                        + ", to: " + value.transactions.get(0).to + ", of " + value.transactions.get(0).value +"KRO! Wow.");
            } else if (result instanceof Result.Err<?>) {
                Result.Err<ListTransactions.ListTransactionsBody> err = (Result.Err<ListTransactions.ListTransactionsBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void MakeTransaction() {
            System.out.println("A test for MakeTransaction is not implemented.");
        }

        static void ListLatestTransactions() {
            Result<ListLatestTransactions.ListLatestTransactionsBody> result = ListLatestTransactions.execute().join();
            if (result instanceof Result.Ok) {
                Result.Ok<ListLatestTransactions.ListLatestTransactionsBody> ok = (Result.Ok<ListLatestTransactions.ListLatestTransactionsBody>) result;
                ListLatestTransactions.ListLatestTransactionsBody value = ok.value();
                System.out.println("From: " + value.transactions.get(0).from
                        + ", to: " + value.transactions.get(0).to + ", of " + value.transactions.get(0).value +"KRO! Wow. ID: " + value.transactions.get(0).id);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<ListLatestTransactions.ListLatestTransactionsBody> err = (Result.Err<ListLatestTransactions.ListLatestTransactionsBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }

        static void GetTransaction() {
            Result<GetTransaction.GetTransactionBody> result = GetTransaction.execute(transactionId).join();
            if (result instanceof Result.Ok) {
                Result.Ok<GetTransaction.GetTransactionBody> ok = (Result.Ok<GetTransaction.GetTransactionBody>) result;
                GetTransaction.GetTransactionBody value = ok.value();
                System.out.println("From: " + value.transaction.from
                        + ", to: " + value.transaction.to + ", of " + value.transaction.value +"KRO! Wow. ID: " + value.transaction.id);
            } else if (result instanceof Result.Err<?>) {
                Result.Err<GetTransaction.GetTransactionBody> err = (Result.Err<GetTransaction.GetTransactionBody>) result;
                System.out.println("Error: " + err.error() + " param: " + err.error().parameter());
            }
        }
    }
}