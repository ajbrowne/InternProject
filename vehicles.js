var conn = new Mongo();
var db = conn.getDB("Specials");

db.dealers.insert({
_id:"53be8f56ee4a2bd5a4c8dc8d",
    name:"Bob Thomas Ford West",
    loc: { coordinates : [ 41.073853, -85.210483 ], type : "Point" },
    numSpecials:8,
    make:["Chevrolet","Cadillac","Buick","GMC"],
    city: "Fort Wayne",
    state:"IN",
    location : { x : 41.073853, y : -85.210483 }
});

db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b47f5",
  year: 2014,
  make : "Cadillac",
  model:"Srx",
  trim :"Nice Car",
  price: 40000,
  urlImage:"http://i.imgur.com/ZH8ANS7.jpg",
  type:"New",
  specs:["17 CITY / 24 HWY"]
});

db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b47f6",
  year: 2014,
  make : "Cadillac",
  model:"CTS",
  trim :"Pretty Good",
  price: 39495,
  urlImage:"http://i.imgur.com/RiL46nX.jpg",
  type:"New",
  specs:["19 CITY / 28 HWY"]
});

db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b47f7",
  year: 2015,
  make : "Cadillac",
  model:"Escalade",
  trim :"Pimpmobile",
  price: 71695,
  urlImage:"http://i.imgur.com/hYyxqLu.jpg",
  type:"New",
  specs:["14 CITY / 21 HWY"]
});
db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b47f8",
  year: 2012,
  make : "Chevrolet",
  model:"Volt",
  trim :"Best",
  price: 34185,
  urlImage:"http://i.imgur.com/Ecc8jaJ.jpg",
  type:"Used",
  specs:["95 CITY / 93 HWY"]
});
db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b47f9",
  year: 2007,
  make : "GMC",
  model:"Yukon",
  trim :"medium",
  price: 1000,
  urlImage:"http://i.imgur.com/cJ6g9rv.jpg",
  type:"Used",
  specs:["14 CITY / 20 HWY"]
});
db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b47fa",
  year: 2014,
  make : "GMC",
  model:"Sierra",
  trim :"BIG ASS TRUCK",
  price: 40000,
  urlImage:"http://i.imgur.com/CRWb2yl.jpg",
  type:"New",
  specs:["18 CITY / 24 HWY"]
});
db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b47fb",
  year: 2005,
  make : "Chevrolet",
  model:"Cobalt",
  trim :"lowest",
  price: 2000,
  urlImage:"http://i.imgur.com/kiqrT2U.jpg",
  type:"Used",
  specs:["22 CITY / 31 HWY"]
});
db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b47fc",
  year: 2007,
  make : "Chevrolet",
  model:"Suburban",
  trim :"the why edition",
  price: 15000,
  urlImage:"http://i.imgur.com/gRNY02E.jpg",
  type:"Used",
  specs:["14 CITY / 19 HWY"]
});
db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b47fd",
  year: 2013,
  make : "GMC",
  model:"Acadia",
  trim :"better",
  price: 7000,
  urlImage:"http://i.imgur.com/RG7la8d.jpg",
  type:"Used",
  specs:["17 CITY / 24 HWY"]
});
db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b47fe",
  year: 2014,
  make : "Chevrolet",
  model:"Impala",
  trim :"Pretty Good",
  price: 17000,
  urlImage:"http://i.imgur.com/tT2pT7S.jpg",
  type:"New",
  specs:["19 CITY / 29 HWY", "Automatic", "Sedan 4 Dr.", "3.6L 6 CYL FUEL INJECTION"]
});
db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b47ff",
  year: 2014,
  make : "Chevrolet",
  model:"Corvette Stringray",
  trim :"Red",
  price: 50000,
  urlImage:"http://i.imgur.com/D3JifZv.jpg",
  type:"New",
  specs:["17 City /  29 HWY"]
});
db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b4800",
  year: 2014,
  make : "Chevrolet",
  model:"Silverado",
  trim :"Big ass truck 2",
  price: 55000,
  urlImage:"http://i.imgur.com/76AsQCJ.jpg",
  type:"New",
  specs:["18 CITY / 24 HWY"]
});
db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b4801",
  year: 2014,
  make : "Chevrolet",
  model:"Camaro",
  trim :"Bumblebee",
  price: 25000,
  urlImage:"http://i.imgur.com/lLeST34.jpg",
  type:"New",
  specs:["17 CITY / 28 HWY"]
});
db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b4802",
  year: 2014,
  make : "Buick",
  model:"Regal",
  trim : "its a Buick",
  price: 24000,
  urlImage:"http://i.imgur.com/0hDoAnJ.jpg",
  type:"New",
  specs:["21 CITY / 30 HWY"]
});
db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b4803",
  year: 2006,
  make : "Buick",
  model:"LaCrosse",
  trim :"meh",
  price: 3000,
  urlImage:"http://i.imgur.com/XlD0emo.jpg",
  type:"Used",
  specs:["17 CITY / 28 HWY"]
});
db.vehicles.insert({
    _id:"53bc30ab4be9fb81694b4804",
  year: 2014,
  make : "Chevrolet",
  model:"Cruze",
  trim :"Good",
  price: 3000,
  urlImage:"http://i.imgur.com/LM43Vci.jpg",
  type:"New",
  specs:["22 CITY / 35 HWY"]
});


db.special.insert({
  title: "Corvette Sale",
  dealer : "53be8f56ee4a2bd5a4c8dc8d",
  amount :"6000",
  status:1,
  vehicleId:["53bc30ab4be9fb81694b47ff"],
  trending :false
});

db.special.insert({
  title: "Cadillac Sale",
  dealer : "53be8f56ee4a2bd5a4c8dc8d",
  amount :"700",
  status:1,
  vehicleId:["53bc30ab4be9fb81694b47f5", "53bc30ab4be9fb81694b47f6", "53bc30ab4be9fb81694b47f7"],
  trending :false
});

db.special.insert({
  title: "Green Sale",
  dealer : "53be8f56ee4a2bd5a4c8dc8d",
  amount :"2000",
  status:1,
  vehicleId:["53bc30ab4be9fb81694b47f8"],
  trending :false
});

db.special.insert({
  title: "Family Deal",
  dealer : "53be8f56ee4a2bd5a4c8dc8d",
  amount :"650",
  status:1,
  vehicleId:["53bc30ab4be9fb81694b47f7", "53bc30ab4be9fb81694b47f9", "53bc30ab4be9fb81694b47fc", "53bc30ab4be9fb81694b47fd"],
  trending :false
});

db.special.insert({
  title: "First Car Special",
  dealer : "53be8f56ee4a2bd5a4c8dc8d",
  amount :"500",
  status:1,
  vehicleId:["53bc30ab4be9fb81694b4804", "53bc30ab4be9fb81694b47fb", "53bc30ab4be9fb81694b4803"],
  trending :false
});

db.special.insert({
  title: "Chevy Summer Sale",
  dealer : "53be8f56ee4a2bd5a4c8dc8d",
  amount :"1000",
  status:1,
  vehicleId:["53bc30ab4be9fb81694b4804", "53bc30ab4be9fb81694b4801", "53bc30ab4be9fb81694b4800", "53bc30ab4be9fb81694b47ff", "53bc30ab4be9fb81694b47fe"],
  trending :false
});

db.special.insert({
  title: "Buick Flash Sale",
  dealer : "53be8f56ee4a2bd5a4c8dc8d",
  amount :"1500",
  status:1,
  vehicleId:["53bc30ab4be9fb81694b4802","53bc30ab4be9fb81694b4803"],
  trending :false
});

db.special.insert({
  title: "Truck Sale",
  dealer : "53be8f56ee4a2bd5a4c8dc8d",
  amount :"637",
  status:1,
  vehicleId:["53bc30ab4be9fb81694b47fa", "53bc30ab4be9fb81694b4800"],
  trending :false
});



