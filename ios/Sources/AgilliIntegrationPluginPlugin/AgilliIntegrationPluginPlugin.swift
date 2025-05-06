import Foundation
import Capacitor

/**
 * Please read the Capacitor iOS Plugin Development Guide
 * here: https://capacitorjs.com/docs/plugins/ios
 */
@objc(AgilliIntegrationPluginPlugin)
public class AgilliIntegrationPluginPlugin: CAPPlugin, CAPBridgedPlugin {
    public let identifier = "AgilliIntegrationPluginPlugin"
    public let jsName = "AgilliIntegrationPlugin"
    public let pluginMethods: [CAPPluginMethod] = [
        CAPPluginMethod(name: "echo", returnType: CAPPluginReturnPromise)
    ]
    private let implementation = AgilliIntegrationPlugin()

    @objc func echo(_ call: CAPPluginCall) {
        let value = call.getString("value") ?? ""
        call.resolve([
            "value": implementation.echo(value)
        ])
    }
}
