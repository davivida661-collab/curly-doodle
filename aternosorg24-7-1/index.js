const mineflayer = require("mineflayer");

//the config
const bot = mineflayer.createBot({
  host: "whitefish.aternos.host", //Coloca entre las comillas, tu ip
  port: 20612, //puerto del servidor
  username: "aternoshost", //nombre del bot (CAMBIAR)
  version: false//version of the server (false = auto detect)
});

bot.on("move", function() {
  //triggers when the bot moves

  bot.setControlState("jump", true); //continuously jumps
  setTimeout(() => {
    //sets a delay
    bot.setControlState("jump", false); //stops jumping
  }, 1000); //delay time

  setTimeout(() => {
    //sets a delay
    bot.setControlState("forward", true); //continuously walks forward
    setTimeout(() => {
      //sets a delay
      bot.setControlState("forward", false); //stops walking forward
    }, 500); //delay time
  }, 1000); //delay time

  setTimeout(() => {
    //sets a delay
    bot.setControlState("back", true); //continuously walks backwards
    setTimeout(() => {
      //sets a delay
      bot.setControlState("back", false); //stops walking backwards
    }, 500); //delay time
  }, 2000); //delay time

  setTimeout(() => {
    //sets a delay
    bot.setControlState("right", true); //continuously walks right
    setTimeout(() => {
      //sets a delay
      bot.setControlState("right", false); //stops walking right
    }, 2000); //delay time
  }, 500); //delay time

  setTimeout(() => {
    //sets a delay
    bot.setControlState("left", true); //continuously walks lefz
    setTimeout(() => {
      //sets a delay
      bot.setControlState("left", false); //stops walking left
    }, 2000); //delay time
  }, 500); //delay time
});
bot.on("end", () => bot()); //riggers when the bot leaves/gets kicked and create a new bot from the config

bot.on("error", err => console.log(err)); //triggers when there's an error and logs it into console
