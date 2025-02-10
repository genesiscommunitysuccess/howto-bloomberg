/**
  * This file contains application specific configuration

  * Full documentation on system definitions may be found here >> https://docs.genesis.global/docs/develop/server-capabilities/runtime-configuration/system-definition/

 */

systemDefinition {
  global {

    /*
     Define configuration items here if you want to allow them to be easily changed globally
     or have different values per environment.
     */
    item("BLOOMBERG_SERVER", "localhost")
    item("BLOOMBERG_SERVER_PORT", 8194)
    item("APPLICATION_NAME", "TestApp")

    /*
     * If you are using a Bloomberg cloud-hosted instance (also known as 'Zero Footprint')
     * you will need to provide data on the security certificates you will be provided in
     * order to connect.
     */
    item("PATH_TO_PK7_CERTIFICATE", "/certs/root.pk7")
    item("PATH_TO_PK12_CERTIFICATE", "certs/cert.pk12")
    item("PK_PASSWORD", "**********")
  }
  systems {
  }
}
