package com.satya.goesjogja.user.data

import com.satya.goesjogja.R
import com.satya.goesjogja.user.model.Kuliner

object KulinerData {

    private val namaKuliner = arrayOf(
        "Sate Klatak",
        "Es Buah PK",
        "Gudeg Pawon",
        "Oseng Mercon Bu Narti",
        "Mangut Lele Mbah Marto",
        "Bakmi Mbah Mo"
            )

    private val deskripsi = arrayOf(" Sate Kambing Khas Bantul ,tempat makan yang satu ini jadi lebih ramai dari biasanya. Tapi memang rasanya begitu enak, empuk dan nggak bau prengus kok! Uniknya, sate klatak ditusuk tidak menggunakan tusuk sate dari bambu seperti biasa, melainkan dengan besi jeruji sepeda! Apalagi disantap bersama gule kambing panas yang bisa bikin udara dingin tak terasa.",
        "Siapa sih yang nggak doyan es buah? Apalagi di tengah cuaca panas dan terik. Warung es buah yang menempati tenda kaki lima ini termasuk legendaris karena sudah ada sejak 44 tahun lalu. Es buah yang bikin orang rela antre ini terdiri dari isian nangka, cingcau hitam, sawo, kelapa muda, alpukat, susu coklat dan parutan es.",
        "Gudeg yang satu ini cukup unik, karena sesuai namanya Anda harus berdesak-desakan di antara pengunjung lain di dapur (pawon) untuk menikmati gudegnya yang lezat. Keunikan lain dari gudeg yang ada sejak tahun 1958 ini adalah bukanya hanya malam hari dan hanya 3 jam saja!",
        "Tak salah memang dinamai “mercon”, karena rasa pedasnya benar-benar meledak di mulut. Berdiri hampir 20 tahun lalu saat negara ini dilanda krisis ekonomi, oseng-oseng racikan Bu Narti ini terdiri dari kikil, daging, gajih, kulit dan tulang muda serta cabe rawit yang melimpah. Siap-siap gobyoss!",
        "Mencari lokasi ini memang bukan perkara mudah, karena Anda harus blusukan ke dalam kampung. Namun setibanya di sana, keramahan Mbah Marto yang telah berjualan sejak tahun 1960an langsung membuat hati adem. Mangut lele sendiri merupakan sajian berupa lele asap dengan kuah santan pedas yang disajikan langsung dari dapur!",
        "Ada satu lokasi lagi yang wajib disinggahi noodle lovers. Beralih ke Bantul, aneka mie godhog (mie rebus) dan bakmi goreng dengan telur bebek dan suwiran ayam kampung yang dimasak di atas tungku arang siap membuat makan malam makin meriah, ditemani kopi jahe hangat atau teh nasgitel yang khas Jogja."
            )

    private val kulinerImages = intArrayOf(
        R.drawable.kuliner_1,
        R.drawable.kuliner_2,
        R.drawable.gudeg,
        R.drawable.kuliner_4,
        R.drawable.kuliner_5,
        R.drawable.kuliner_3
            )

    val listKuliner: ArrayList<Kuliner> //jangan dirubah
        get(){
                val list = arrayListOf<Kuliner>()
                for (position in namaKuliner.indices){
                        val kuliner = Kuliner()
                        kuliner.name = namaKuliner[position]
                        kuliner.deskripsi = deskripsi[position]
                        kuliner.images = kulinerImages[position]
                        list.add(kuliner)
                }
                return list
        }
}