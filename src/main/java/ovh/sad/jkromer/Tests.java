package ovh.sad.jkromer;

import ovh.sad.jkromer.http.Result;
import ovh.sad.jkromer.http.addresses.*;
import ovh.sad.jkromer.http.misc.GetMotd;
import ovh.sad.jkromer.http.misc.GetSupply;
import ovh.sad.jkromer.http.names.*;

public class Tests {

    public static class Addresses {
        static void GetAddress() {
            Result<GetAddress.GetAddressBody> result = GetAddress.execute("kmmx7kcr4o").join();
            if (result instanceof Result.Ok(GetAddress.GetAddressBody value)) {
                System.out.println("Address: " + value.address.balance);
            } else if (result instanceof Result.Err(Errors.ErrorResponse error)) {
                System.out.println("Error: " + error + " param: " + error.parameter());
            }
        }

        static void ListAddresses() {
            var result = ListAddresses.execute().join();
            if (result instanceof Result.Ok(ListAddresses.ListAddressesBody value)) {
                System.out.println("Address: " + value.addresses.getFirst().address);
            } else if (result instanceof Result.Err(Errors.ErrorResponse error)) {
                System.out.println("Error: " + error + " param: " + error.parameter());
            }
        }
        static void RichAddresses() {
            var result = RichAddresses.execute().join();
            if (result instanceof Result.Ok(RichAddresses.RichAddressesBody value)) {
                System.out.println("Address: " + value.addresses.getFirst().address + ", balance: " + value.addresses.getFirst().balance + "KRO! Wow.");
            } else if (result instanceof Result.Err(Errors.ErrorResponse error)) {
                System.out.println("Error: " + error + " param: " + error.parameter());
            }
        }
        static void GetAddressTransactions() {
            var result = GetAddressTransactions.execute("kmmx7kcr4o").join();
            if (result instanceof Result.Ok(GetAddressTransactions.GetAddressTransactionsBody value)) {
                System.out.println("From: " + value.transactions.getFirst().from
                        + ", to: " + value.transactions.getFirst().to + ", of " + value.transactions.getFirst().value +"KRO! Wow.");
            } else if (result instanceof Result.Err(Errors.ErrorResponse error)) {
                System.out.println("Error: " + error + " param: " + error.parameter());
            }
        }
        static void GetAddressNames() {
            var result = GetAddressNames.execute("kmmx7kcr4o").join();
            if (result instanceof Result.Ok(GetAddressNames.GetAddressNamesBody value)) {
                System.out.println("Current owner: " + value.names.getFirst().owner
                        + ", Oriignal owner: " + value.names.getFirst().original_owner + ", A record: " + value.names.getFirst().a);
            } else if (result instanceof Result.Err(Errors.ErrorResponse error)) {
                System.out.println("Error: " + error + " param: " + error.parameter());
            }
        }
    }

    public static class Misc {
        static void GetMotd() {
            var result = GetMotd.execute().join();
            if (result instanceof Result.Ok(GetMotd.GetMotdBody value)) {
                System.out.println("Version " + value.motdPackage.version + " of " + value.motdPackage.name + " downloadable at " + value.motdPackage.repository + ".");
            } else if (result instanceof Result.Err(Errors.ErrorResponse error)) {
                System.out.println("Error: " + error + " param: " + error.parameter());
            }
        }
        static void Login() {
            // not hardcoding a privatekey
            System.out.println("A test for Login is not implemented.");
        }
        static void GetSupply() {
            var result = GetSupply.execute().join();
            if (result instanceof Result.Ok(GetSupply.GetSupplyBody value)) {
                System.out.println("Supply: " + value.money_supply);
            } else if (result instanceof Result.Err(Errors.ErrorResponse error)) {
                System.out.println("Error: " + error + " param: " + error.parameter());
            }
        }
    }

    public static class Names {
        static void GetName() {
            Result<GetName.GetNameBody> result = GetName.execute("cock").join();
            if (result instanceof Result.Ok(GetName.GetNameBody value)) {
                System.out.println("Owner: " + value.name.owner);
            } else if (result instanceof Result.Err(Errors.ErrorResponse error)) {
                System.out.println("Error: " + error + " param: " + error.parameter());
            }
        }
        static void ListNames() {
            Result<ListNames.ListNamesBody> result = ListNames.execute().join();
            if (result instanceof Result.Ok(ListNames.ListNamesBody value)) {
                System.out.println("Owner: " + value.names.getFirst().owner);
            } else if (result instanceof Result.Err(Errors.ErrorResponse error)) {
                System.out.println("Error: " + error + " param: " + error.parameter());
            }
        }
        static void ListNewestNames() {
            Result<ListNewestNames.ListNewestNamesBody> result = ListNewestNames.execute().join();
            if (result instanceof Result.Ok(ListNewestNames.ListNewestNamesBody value)) {
                System.out.println("Owner: " + value.names.getFirst().owner);
            } else if (result instanceof Result.Err(Errors.ErrorResponse error)) {
                System.out.println("Error: " + error + " param: " + error.parameter());
            }
        }
        static void GetNameCost() {
            var result = GetNameCost.execute().join();
            if (result instanceof Result.Ok(GetNameCost.GetNameCostBody value)) {
                System.out.println("Cost: " + value.name_cost);
            } else if (result instanceof Result.Err(Errors.ErrorResponse error)) {
                System.out.println("Error: " + error + " param: " + error.parameter());
            }
        }
        static void GetNameAvailability() {
            var unavailable = GetNameAvailability.execute("cock").join();
            if (unavailable instanceof Result.Ok(GetNameAvailability.GetNameAvailabilityBody value)) {
                System.out.println("Availability: " + value.available + " (should be false)");
            } else if (unavailable instanceof Result.Err(Errors.ErrorResponse error)) {
                System.out.println("Error: " + error + " param: " + error.parameter());
            }
            var available = GetNameAvailability.execute("cock2").join();
            if (available instanceof Result.Ok(GetNameAvailability.GetNameAvailabilityBody value)) {
                System.out.println("Availability: " + value.available + " (should be true)");
            } else if (available instanceof Result.Err(Errors.ErrorResponse error)) {
                System.out.println("Error: " + error + " param: " + error.parameter());
            }
        }
    }
}
