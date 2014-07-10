conn = new Mongo();
db = conn.getDB("Specials");

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
