package com.satya.goesjogja.user.data

import com.satya.goesjogja.R
import com.satya.goesjogja.user.model.Event

object EventData {
    private val namaEvent = arrayOf(
        "HARMONI CINA-JAWA",
        "Nandur Srawung",
        "Maestro Meeting",
        "Pelesiran di Malioboro",
        "Yogyakarta Ultra Cycling Challenge* 200KM & 600KM"
            )

    private val tanggal = arrayOf(
        "26 feb-27 mar 2021 ",
        "10-19 sep 2021",
        "19 Des 2021 -9 Jan 2022",
        "18 Desember 2021",
        "29 Januari 2022"
            )

    private val deskripsi = arrayOf("Masih dalam suasana Tahun Baru Imlek ke-2572, Museum Sonobudoyo kembali menggelar pameran sebagai ruang eksplorasi koleksi. Pagelaran pameran ini menjadi agenda tahunan yang di gelar oleh Museum Sonobudoyo. Pameran kali ini bertajuk 'Harmoni Cina-Jawa dalam Seni Pertunjukan'. Pameran ini mengangkat sebuah akulturasi dua budaya dalam sebuah seni pertunjukan yaitu Cina dan Jawa. Salah satu koleksi yang akan tampil dalam pameran ini ialah wayang tokoh Sie Jin Kwie berjubah putih yang sedang menunggang seekor kuda.",
        "Nandur Srawung kembali dihadirkan menginjak tahun ke-8 yang kali ini mengambil tema Ecosystem: Pranatamangsa, dan merupakan penyelenggaraan Nandur Srawung kedua di tengah masa pandemi. Menghadirkan total 167 seniman (individu dan kelompok – juga termasuk keterlibatan seniman mancanegara dari Austria, Swiss, Jepang, dan Jerman) dengan menyuguhkan 72 karya. Jenjang usia para seniman juga sangat beragam, dari 4 tahun hingga 60 tahun, yang menjadikan Nandur Srawung kali ini lebih menarik.",
		"Patronase seni yang mewarnai sejarah awal perkembangan seni rupa modern Indonesia mencerminkan adanya kesatuan pandangan antara elite dan seniman tentang pentingnya seni bagi kehidupan negara bangsa dan masyarakat. Seni rupa modern Indonesia tumbuh dan berkembang dalam alam revolusi kemerdekaan dan bersama dengan idealisme para senimannya. Secara simbolik Dyan Anggraini menampilkan patronase dan idealisme seni ini secara elok dan mengesankan.",
		"Malioboro bukan hanya sekedar jalan, namun ada segudang cerita dan simbol-simbol yang disematkan oleh pendiri Kasultanan Yogyakarta, Sri Sultan Hamengku Buwono I pada pertengahan Abad 18.",
		"Awal tahun ini akan dibuka dengan tantangan 600km & 200km di Yogyakarta. Kota ini adalah kota favorit kami karena kota ini mudah dijangkau, kota yang ramah dan kota yang banyak pilihan kulinernya."
            )

    private val lokasi = arrayOf("Gedung Pameran Temporer Museum Sonobudoyo",
            "Taman Budaya Yogyakarta","Jogja Gallery","Sepanjang Kawasan Malioboro","Jogja National Museum"
            )

    private val eventImages = intArrayOf(
        R.drawable.event_1,
        R.drawable.event_2,
        R.drawable.event_3,
        R.drawable.event_4,
        R.drawable.event_5
            )

    val listEvent: ArrayList<Event>
        get(){
                val list = arrayListOf<Event>()
                for (position in namaEvent.indices){
                        val event = Event()
                        event.name = namaEvent[position]
                        event.tanggal = tanggal[position]
                        event.deskripsi = deskripsi[position]
                        event.lokasi = lokasi[position]
                        event.images = eventImages[position]
                        list.add(event)
                }
                return list
        }
}