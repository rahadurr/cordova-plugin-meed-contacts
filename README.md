# cordova-plugin-meed-contacts
This is the Cordova plugin for Read Native Contacts from **iOS**, & **Android** for [MeedBankingClub](https://meedbankingclub.com) App.

## Supported Platform:
- \[x] iOS.
- \[X] Android.

## Changelog:
Initial release.

## Installation:

```console
ionic cordova plugin add cordova-plugin-meed-contacts

npm install @meed-native/contacts
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

```typescript
// app.module.ts
import { Contacts } from '@meed-native/contacts/ngx';

@NgModule({
  ...

  providers: [
    ...
    Contacts
    ...
  ]
  ...
})
export class AppModule { }
```


### Fetch All contacts
To fetch all contacts from user device. We can use 
`all(options: Options)`
method from plugins.

```typescript
// home.page.ts file

import { Component } from '@angular/core';
// Import meed native wrapper 
import { Contacts, Options, Contact } from '@meed-native/contacts/ngx';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {
  constructor(
    private contacts: Contacts
  ) {}

  loadAllContact() {
    const options: Options = {"limit": 10, "skip": 0};

    this.contacts.all(options)
    .then((contacts: Contact[]) => {
      console.log(contacts);
    })
    .catch((error: any) => {
      console.log(error)
    });
  }
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
`search(searchText: string, options: Options)`
method from plugin.
```typescript
// home.page.ts file

import { Component } from '@angular/core';
// Import meed native wrapper 
import { Contacts, Options, Contact } from '@meed-native/contacts/ngx';

@Component({
  selector: 'app-home',
  templateUrl: 'home.page.html',
  styleUrls: ['home.page.scss'],
})
export class HomePage {
  constructor(
    private contacts: Contacts
  ) {}

  searchContact(ev: any) {
    const searchText = ev.target.value;

    const options: Options = { "limit": 10, "skip": 0 };

    this.contacts.search(searchText, options)
      .then((contacts: Contact[]) => {
        console.log(contacts);
      }).catch((error: any) => {
        console.log(error);
        
      });
  }
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

### Interfaces:
```typescript 
interface Contact {
    name: string;
    emails: string[];
}
```

```typescript 
interface Options {
    limit: number;
    skip: number;
}
```

### Actions:
`all(options: Options)`

`search(searchText: String, options: Options)`


## License
[The MIT License](./LICENSE.md)