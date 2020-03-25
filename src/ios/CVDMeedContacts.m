/********* Contacts.m Cordova Plugin Implementation *******/

#import "CVDMeedContacts.h"


@implementation CVDMeedContacts

- (void)all:(CDVInvokedUrlCommand*)command
{
    __block CDVPluginResult* pluginResult = nil;
    
    // Recived Object from Meed client app
    NSDictionary* options = [command.arguments objectAtIndex:0];

    if (options != nil) {
//        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsString:[options objectForKey:@"skip"]];
        [self.commandDelegate runInBackground:^{
            CVDMeedContact *contact = [CVDMeedContact new];
            NSArray *contacts = [contact all:options];
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:contacts];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        }];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    // [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}




- (void)search:(CDVInvokedUrlCommand *)command
{
    __block CDVPluginResult* pluginResult = nil;
    NSString* searchString = [command.arguments objectAtIndex:0];
    NSDictionary* options = [command.arguments objectAtIndex:1];
    
    if (options != nil) {
        [self.commandDelegate runInBackground:^{
            CVDMeedContact *contact = [CVDMeedContact new];
            NSArray *contacts = [contact search:searchString searchOptios:options];
            pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_OK messageAsArray:contacts];
            [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
        }];
    } else {
        pluginResult = [CDVPluginResult resultWithStatus:CDVCommandStatus_ERROR];
    }

    // [self.commandDelegate sendPluginResult:pluginResult callbackId:command.callbackId];
}
@end
