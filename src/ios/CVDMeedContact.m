#import "CVDMeedContact.h"

@implementation CVDMeedContact

- (NSArray*)all:(NSDictionary *)command
{
    long limit = [[command valueForKey:@"limit"] integerValue];
    long skip = [[command valueForKey:@"skip"] integerValue];
    
    __block NSMutableArray *allContacts = nil;
    CNContactStore *store = [CNContactStore new];
    
    [store requestAccessForEntityType:CNEntityTypeContacts completionHandler:^(BOOL granted, NSError * _Nullable error) {
        if (granted == YES) {
            NSArray *keys = @[CNContactGivenNameKey, CNContactFamilyNameKey, CNContactEmailAddressesKey];
            NSString *containerId = [store defaultContainerIdentifier];
            NSPredicate *predicate = [CNContact predicateForContactsInContainerWithIdentifier:containerId];
            NSError *error;

            NSArray *contacts = [store unifiedContactsMatchingPredicate:predicate keysToFetch:keys error:&error];
            
            NSArray *rangeContacts;
            
            if ([contacts count] > skip && [contacts count] > (skip + limit)) {
                  rangeContacts = [contacts subarrayWithRange:NSMakeRange(skip, limit)];
            } else if([contacts count] > skip) {
                rangeContacts = [contacts subarrayWithRange:NSMakeRange(skip, [contacts count] - skip)];
            } else {
                return;
            }
            
            if (error) {
                NSLog(@"Error fetching contacts %@", error);
            } else {
                allContacts = [NSMutableArray array];
                for (CNContact *contact in rangeContacts) {
                    if (contact.emailAddresses.count > 0) {
                        NSMutableArray *emails = [NSMutableArray array];
                        for (CNLabeledValue<NSString*> *email in contact.emailAddresses) {
                            [emails addObject:email.value];
                        }
                        NSDictionary *newContact = @{
                            @"name": [NSString stringWithFormat:@"%@ %@", contact.givenName, contact.familyName],
                            @"emails": emails
                        };
                        [allContacts addObject:newContact];
                    }
                
                }
            }
        }
    }];
    
    return allContacts;
    
}


- (NSArray*)search:(NSString *)searchString searchOptios:(NSDictionary *)options
{
    // long limit = [[options valueForKey:@"limit"] integerValue];
    // long skip = [[options valueForKey:@"skip"] integerValue];
    
    if ([searchString length] == 0) {
        return [NSArray array];
    }
    
    __block NSMutableArray *allContacts = nil;
    CNContactStore *store = [CNContactStore new];
    
    [store requestAccessForEntityType:CNEntityTypeContacts completionHandler:^(BOOL granted, NSError * _Nullable error) {
        if (granted == YES) {
            NSArray *keys = @[CNContactGivenNameKey, CNContactFamilyNameKey, CNContactEmailAddressesKey];
            NSPredicate *predicate = [CNContact predicateForContactsMatchingName:searchString];
            NSError *error;
            
            NSArray *contacts = [store unifiedContactsMatchingPredicate:predicate keysToFetch:keys error:&error];
            
            if (error) {
                NSLog(@"Error fetching contacts %@", error);
                return;
            } else {
                if ([contacts count] > 0) {
                    allContacts = [NSMutableArray array];
                    for (CNContact *contact in contacts) {
                        if (contact.emailAddresses.count > 0) {
                            NSMutableArray *emails = [NSMutableArray array];
                            for (CNLabeledValue<NSString*> *email in contact.emailAddresses) {
                                [emails addObject:email.value];
                            }
                            NSDictionary *newContact = @{
                                @"name": [NSString stringWithFormat:@"%@ %@", contact.givenName, contact.familyName],
                                @"emails": emails
                            };
                            [allContacts addObject:newContact];
                        }
                    }
                } else {
                    return;
                }
            }
        }
    }];
    
    return allContacts;
}

@end
