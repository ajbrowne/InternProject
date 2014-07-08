conn = new Mongo();
db = conn.getDB("Specials");

db.vehicles.insert({
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
  year: 2014,
  make : "Chevrolet",
  model:"Cruze",
  trim :"Good",
  price: 3000,
  urlImage:"http://i.imgur.com/LM43Vci.jpg",
  type:"New",
  specs:["22 CITY / 35 HWY"]
});



