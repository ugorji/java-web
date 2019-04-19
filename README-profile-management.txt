Profile Information (managed outside of the forum, shared by other apps too)
============================================================================
- profile information is basically a generic map of String to String[]

- If apps want to share, they can share the same backend files.

Base Profile:
- Username
- Display Name
- email address
- web page
- registration date
- Avatar

Forum adds:
- Num Messages Posted
- ...

Try to use the same keys from portlet preferences

Features
- Mass emailing of groups and users by the administrator

Make a User Preferences Module
- Each user has a map of String to String[]
- An interface is used for this
- It is possible to put it in the Portlet, or on disk
- U can get a user's preference, or preference for everyone

The module defines 3 interfaces, which has methods to get
Interface: net.ugorji.oxygen.util.UserPreferencesManager
- String[]              getUsers()
- Map<String, String[]> getForAllUsers()
- Map<String, String[]> getForKey(String key)
- Map<String, String[]> getForUser(String username)
- String[]              getForUser(String username, String key)
- void                  setForUser(String username, String key, String[] values)
Interface: net.ugorji.oxygen.util.GroupManager
- boolean               isUserInGroup(String user, String group)
- void                  addUserToGroup(String user, String group)
- String[]              getUsersInGroup(String group)
- void                  setUsersInGroup(String group, String[] users)
- void                  removeGroup(String group)
Interface: net.ugorji.oxygen.util.UserPasswordManager
- String                getEncryptedPassword(String username)
- void                  setEncryptedPassword(String username, String encPasswd)

Implementations can skip implementing the setXXX methods.

An implementation uses:
- A special sequence of characters, to define the separator
- 3 files: 1 for user-specific preferences, 1 for group info, 1 for passwd info
  - the group info and passwd info file, are like what Apache Httpd uses
- Each time a get is requested, check to ensure the file has not been modified after
  - it can load up info from a resource.
- def impl uses files
  Default${intfname}.java

It also comes with a class for Tomcat authentication (as a realm).
- only leverages the INTERFACE 2, 3

  
Default Preferences Stored
==========================
- None

