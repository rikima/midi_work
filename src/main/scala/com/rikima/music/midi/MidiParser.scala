package com.rikima.music.midi

import java.io.File
import de.sciss.midi._

/**
  * Created by masakir on 2016/06/25.
  */
object MidiParser {
  implicit val rate = TickRate.tempo(bpm = 120, tpq = 1024)

  def getSequence(midi: File): Sequence = {
    Sequence.read(midi.getAbsolutePath)
  }

  def getEvents(midi: File): Seq[Seq[Event]] = {
    val sq  = Sequence.read(midi.getAbsolutePath)
    sq.tracks.map {
      case t =>
        t.events
    }
  }

  def execute(midi: File): Unit = {

    val sq  = Sequence.read(midi.getAbsolutePath)
    val pl  = Sequencer.open()
    pl.play(sq)
    pl.stop()
    val t   = sq.tracks(1)  // second of three tracks
    val ev  = t.events      // all events in that track
    ev.zipWithIndex.foreach {
      case (e, i)  =>
        println(s"#$i ${e.toString}")
    }

    val pch = ev.collect { case Event(_, NoteOn(_, pch, _)) => pch }  // pitches
    pch.map(_ % 12).toSet.toList.sorted // pitch classes (all twelve!)

  }

  def main(args:Array[String]): Unit = {
    var midiFile: File = null
    for (i <- 0 until args.length) {
      val a = args(i)
      if (a == "-i" || a == "--input") {
        midiFile = new File(args(i+1))
      }
    }
    if (midiFile == null) {
      println("please input midi file")
      System.exit(1)
    }
    execute(midiFile)
  }
}
