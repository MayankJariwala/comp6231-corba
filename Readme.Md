# COMP6231 - Corba Assignment 2 (5%)

# Team Members:
  - Love Grewal
  - Mayank Jariwala

[//]: Variables
   [Corba Plugin Link]: <http://eclipsecorba.sourceforge.net/update_unstable>
   [IDL Compiler Image]: <https://ibb.co/tQ84MfN>
   [ORB Compiler Image]: <https://ibb.co/bmmYLT8>
# IDE
    - Eclipse IDE for Java Developers.
    - Version: Photon Release (4.8.0).
# Prerequesite
  - Setting up corba in eclipse steps:
    1. Eclipse -> Help -> Install New Software.
    2. Click Add: Enter Name (Your choice) and corba link [Corba Plugin Link].
    3. Proceed with next option, you may get security warning but just ignore and finally finish by accepting terms.
    4. Restart Eclipse

# Configuration
Create an configuration for IDL Compiler [IDL Compiler Image] and ORB [ORB Compiler Image]

#### IDL File - ManagerCommunication.idl
```
module concordia {
	module dems {
		module communication { 
			interface IEventManagerCommunication{
				string performOperation(in string userRequest);
				oneway void shutdown();
			};
		};
	};
};
```
