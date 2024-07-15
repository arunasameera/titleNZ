# Security / AppSec focused interview

For a security-focused interview, please examine the code samples below and comment on whatever flaws they may or may not have from a security point-of-view.

## Sample 1 - Auth

This api method is used when logging in. What potential issues can be seen within this code?

```kotlin
@PostMapping("/auth/api/login", produces = ["application/json"])
fun login(
    @Valid @RequestBody loginData: LoginRequest,
    request: HttpServletRequest,
    response: HttpServletResponse
): ResponseEntity<Any> {
    logger.info("Authenticating ${loginData.userID}")

    val result =
        loginService.checkLogin(username = loginData.userID, password = loginData.password)
    logger.info("Authenticating result: ${loginData.userID}: $result")

    if (result == AuthStatus.LOCKED) { // lock for 15 minutes after 3 failed attempts
        loginService.logUserActivity(
            loginData.userID,
            LogOnLogOffCode.LGON,
            false,
            UserActivityMonitoringCode.BDPW
        )
        return ResponseEntity(AuthErrorResponse(errorMessage = authStatus.description), HttpStatus.UNAUTHORIZED)
    }

    if (result == AuthStatus.FAIL) {
        loginService.logUserActivity(
            loginData.userID,
            LogOnLogOffCode.LGON,
            false,
            UserActivityMonitoringCode.BDPW
        )
        incrementLockoutAttempts(); // increment failed attempts
        return ResponseEntity(AuthErrorResponse(errorMessage = authStatus.description), HttpStatus.UNAUTHORIZED)
    }

    loginService.logUserActivity(loginData.userID, LogOnLogOffCode.LGON, true)
    createSession(
        userId = loginData.userID,
        passwordChangeRequired = false,
        response = response,
        isSecure = request.isSecure
    )

    return ResponseEntity(HttpStatus.OK)
}
```

## Sample 2 - Logging

This code is invoked when a login is successful. What concerns from a security perspective exist? And how might these be mitigated?

```kotlin
fun createSession(
    userId: String,
    passwordChangeRequired: Boolean,
    response: HttpServletResponse,
    isSecure: Boolean
) {
    eventHubManager.logToEventHub(log, Level.INFO, "creating session for user "+userId)
    log.info("creating session for user "+userId)

    val requiredActions = mutableListOf<AuthAction>()
    if (passwordChangeRequired) requiredActions.add(AuthAction.CHANGE_PASSWORD)

    val dbUser = userRepository.getUserInfo(userId)
    eventHubManager.logToEventHub(log, Level.INFO, "user info: "+dbUser.asString())
    log.info("user info: "+dbUser.asString())

    val session = AuthSession(
        userId = userId,
        requiredActions = requiredActions,
        firms = dbUser.associatedFirmRelationships.map { it ->
            SessionFirm(
                id = it.firm.id,
                name = it.firm.corporateName,
                privileges = userRepository.getFirmUserPrivileges(it)
                    .map { privilege -> privilege.toString().toUpperCase() },
            )
        },
        categories = dbUser.roles
    )
    val sessionId = sessionService.saveSession(session = session)
    
    eventHubManager.logToEventHub(log, Level.INFO, "auth session: "+sessionId+":"+session.asString())
    log.info("auth session: "+sessionId+":"+session.asString())

    setSessionCookie(response, sessionId, isSecure)
}
```

## Sample 3 - Database Operation

Here is some code that retrieves contact details from the database. What might be wrong with this code, and in what scenarios can these issues be exploited?

```kotlin
@Repository
class ContactDetailRepository(@Informix db: Database) : DtoRepository<ContactDetail>(ContactDetail::class.java, db) {

    /**
     * Get the contact details for a user using the cf_cde_s15_getusercontactdetails function
     */
    fun getUserContactDetails(userId: String, firmId: String?): ContactDetail {
        val sql = """
            execute function cf_cde_s15_getusercontactdetails(as_user_id= $userId,
            as_firm_id = $firmId, avc_delivery_method = :deliveryMethod)
        """.trimIndent()
        return find(sql)
            .setParameter("deliveryMethod", "EMAL")
            .findOne()!!
    }

}
```

## Sample 4 - Database Healthcheck

This is some code used to verify that database network connectivity is functioning as expected. What issue can be seen in the below code, and how would it possibly be exploited? What sort of mitigations would you suggest?

```kotlin
/**
 * Log the count and details of any connections to the Landonline database
 */
private fun checkConnections(port String) {
    val os_name: String = System.getProperty("os.name")
    val connectionDetails = if (os_name.contains("Windows"))
        execute("cmd", "/c", "netstat -n | findstr :$port").trim() else
        execute("sh", "-c", "netstat -n | grep :$port").trim()
    val numConnections = if (connectionDetails.isBlank()) 0 else connectionDetails.lines().size
    logger.trace("Connections: $numConnections")
    if (numConnections > 0) {
        logger.trace("\n  $connectionDetails")
    }
}
```
