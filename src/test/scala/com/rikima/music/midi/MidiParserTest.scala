package com.rikima.music.midi

import java.io.File

import de.sciss.midi.{Sequence, TickRate, Track}
import de.sciss.midi.Event
import org.junit.Test

/**
  * Created by masakir on 2016/06/25.
  */
class MidiParserTest {
  @Test
  def testExecute(): Unit = {
    val midi = new File("../../data/moon.midi")
    MidiParser.execute(midi)
  }

  @Test
  def testCreateMidi(): Unit = {
    val midi = new File("../../data/moon.midi")
    val evs = MidiParser.getEvents(midi)
    implicit val rate = TickRate.tempo(bpm = 120, tpq = 1024)

    val ts = evs.map {
      case es: Seq[Event] =>
        Track(es.toVector)
    }
    val sq = Sequence(ts.toVector)

    sq.write("../../data/copied_" + midi.getName)
  }

}
