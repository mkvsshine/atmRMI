import java.rmi.*;  
/**
   * Client program for the bank demo
   * @param argv The command line arguments which are ignored.
   */
public class bankClient  {
   
  public static void main (String[] argv) {
    try {
  // check if the argument is provided
      if (argv.length < 2) {
		   System.out.println ("Usage: java bankClient host:port opCmd arg1 arg2...");
		   return;
	  }

	  String op, server, dest;

	  server = argv[0];
	  op = argv[1];
	  dest = "//" + server+"/mybank";
	  	
      bankInterface  bank = 
        (bankInterface) Naming.lookup (dest);

	  if (op.equals("deposit"))
	  {
		  if (argv.length != 4) {
			 System.out.println("Usage: java bankClient host:port deposit account amount");
			 return;
		  }
		  try
		  {
			int account = Integer.parseInt(argv[2]);
			int amount = Integer.parseInt(argv[3]);

            if (! checkAmount(amount))
                return;

	
            CODE err = bank.deposit(account, amount);
            if (err != CODE.OK) {
                readErrorCode(err, account);

                return;
            }

            System.out.printf("Successfully deposit $%d to account %d!\n", amount, account);

		  } catch(NumberFormatException e)
		  {
			  System.out.println("Input Error: Please input integer amount only!");
			  return;
		  }
	  }
	  else if (op.equals("withdraw"))
	  {
          if (argv.length != 4) {
              System.out.println("Usage: java bankClient host:port withdraw account amount");
              return;
          }
          try
          {
              int account = Integer.parseInt(argv[2]);
              int amount = Integer.parseInt(argv[3]);

              if (! checkAmount(amount))
                return;


              CODE err = bank.withdraw(account, amount);
              if (err != CODE.OK) {
                  readErrorCode(err, account);

                  return;
              }

              System.out.printf("Successfully withdraw $%d from account %d!\n", amount, account);

          } catch(NumberFormatException e)
          {
              System.out.println("Input Error: Please input integer amount only!");
              return;
          }
	  }
	  else if (op.equals("transfer"))
	  {
          if (argv.length != 5) {
              System.out.println("Usage: java bankClient host:port transfer From To amount");
              return;
          }
          try
          {
              int accountFrom = Integer.parseInt(argv[2]);
              int accountTo = Integer.parseInt(argv[3]);
              int amount = Integer.parseInt(argv[4]);

              if (! checkAmount(amount))
                  return;

              CODE err = bank.transfer(accountFrom, accountTo, amount);
              if (err != CODE.OK) {
                  readErrorCode(err, accountFrom, accountTo);

                  return;
              }
              System.out.printf("Successfully transfer $%d from account %d to account %d!\n", amount, accountFrom, accountTo);

          } catch(NumberFormatException e)
          {
              System.out.println("Input Error: Please input integer amount only!");
              return;
          }
	  }
	  else if (op.equals("inquiry"))
	  {
          if (argv.length != 3) {
              System.out.println("Usage: java bankClient host:port inquiry account");
              return;
          }
          try
          {
              int account = Integer.parseInt(argv[2]);

              int amount = bank.inquiry(account);
              if (amount < 0) {
                  readErrorCode(account);
                  return;
              }

              System.out.printf("The current balance of user %d is $%d\n", account, amount);

          } catch(NumberFormatException e)
          {
              System.out.println("Input Error: Please input integer amount only!");
              return;
          }

	  }
	  else 
	  {
		  System.out.println("Operation Not Defined:"+op);
		  return;
	  }

	

       // System.out.printf ("Dest=%s op=%s\n", dest, op);
       // System.out.printf ("Amt=%d\n", bank.inquiry(200));

    } catch (Exception e) {
      System.out.println ("bankClient exception: " + e);
    }
  }

    private static boolean checkAmount(int amount) {
        if (amount <= 0) {
            System.out.println("Illegal amount! Please make sure your amount is positive!");
            System.out.println("Amount="+amount);
            return false;
        }

        return true;
    }

    // for method inquiry, only one possible error
    private static void readErrorCode(int account) {
        System.out.printf("No such user or account, %d!\n", account);
    }

    // for method withdraw and deposit
    private static void readErrorCode(CODE err, int account)  {
        if (err == CODE.UNKNOWN_ACCOUNT)    {
            System.out.printf("No such user or account, %d!\n", account);
        }
        else if (err == CODE.NO_SUFFICIENT_FUND) {
            System.out.printf("No sufficient fund in account %d!\n", account);
        }
        else {
            // if other situation happens
            System.out.printf("ERROR=%s\n", err.toString());
        }
    }

    // for method transfer
    private static void readErrorCode(CODE err, int account1, int account2) {
        if (err == CODE.UNKNOWN_ACCOUNT)    {
            System.out.printf("No such user or account, %d!\n", account1);
        }
        else if (err == CODE.UNKNOWN_ACCOUNT2) {
            System.out.printf("No such user or account, %d!\n", account2);
        }
        else if (err == CODE.NO_SUFFICIENT_FUND) {
            System.out.printf("No sufficient fund in account %d!\n", account1);
        }
        else {
            // if other situation happens
            System.out.printf("ERROR=%s\n", err.toString());
        }

    }


}
