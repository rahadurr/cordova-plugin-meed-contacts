# cordova-plugin-meed-contacts
This is the Cordova plugin for Read Native Contacts from **iOS**, & **Android** for [MeedBankingClub](https://meedbankingclub.com) App.

## Supported Platform:
- \[x] iOS.
- \[X] Android.

## Changelog:
Initial release.

## Installation:

```console
cordova plugin add cordova-plugin-meed-contacts
```

## Permission Required:

```xml
// iOS
<key>NSContactsUsageDescription</key>
<string>need contacts access to search friends</string>

// Android
<uses-permission android:name="android.permission.READ_CONTACTS" />
```

## Getting Start:

First of all declare plugin variable in a .ts file where you want to access plugin methods.


```typescript
// home.page.ts file

import { Component } from '@angular/core';

declare var meed;  // Plugin variable

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
```


### Fetch All contacts
To fetch all contacts from user device. We can use 
`meed.plugins.all(options, successCallback, errorCallback)`
method from plugins.

```typescript
loadAllContact() {

    const options = {"limit": 10, "skip": 5}

    meed.plugins.Contacts.all( options,
    , (contacts) => {
      console.log(contacts);
    }, (error) => {
      console.error(error);
    });
}
```

### Response
```console
[
    {name: "John Appleseed", emails: ["John-Appleseed@mac.com"]}, 
    {name: "Anna Haro", emails: ["anna-haro@mac.com"]},
    {name: "Kate Bell", emails: ["kate-bell@mac.com", "kate-belll-home@mac.com"]}
    ...
    ...
    ...
]
```


### Search Contacts:
To search contact by charecter we can use
`meed.plugins.search(searchString, options, successCallback, errorCallback)`
method from plugin.
```typescript
searchContact(ev: any) {
    const searchString = ev.target.value;

    const options = { "limit": 10, "skip": 0 };


    meed.plugins.Contacts.search(searchString, options, (res) => {
      console.log(res);
    }, (error) => {
      console.error(error);
    });
  }
```
### Response
```console
[
    {name: "John Appleseed", emails: ["John-Appleseed@mac.com"]}, 
    {name: "Anna Haro", emails: ["anna-haro@mac.com"]},
    {name: "Kate Bell", emails: ["kate-bell@mac.com", "kate-belll-home@mac.com"]}
]
```


## API

### Options:
```typescript 
interface Option {
    limit: number;
    skip: number;
}
```

### Actions:
`meed.plugins.all(options, successCallback, errorCallback)`

`meed.plugins.search(searchString, options, successCallback, errorCallback)`


## License
[The MIT License](./LICENSE.md)