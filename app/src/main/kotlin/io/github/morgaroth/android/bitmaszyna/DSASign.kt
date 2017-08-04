package io.github.morgaroth.android.bitmaszyna

/**
 * Created by morgaroth on 02.08.17.
 */
import android.util.Base64
import java.math.BigInteger
import java.security.KeyFactory
import java.security.Signature
import java.security.spec.*

object ECDSAExample {

    fun sign(data: String): String {

        val dsa = Signature.getInstance("SHA1withECDSA")
        val kf = KeyFactory.getInstance("EC")


        val decodedKey: ByteArray = Base64.decode(Keys.ecdsaKey.toByteArray(), Base64.NO_WRAP)
        val decodedKeyInt = BigInteger(decodedKey)
//        val priv = DSAPrivateKeySpec(decodedKeyInt, BigInteger.ZERO, BigInteger.ZERO, BigInteger.ZERO)
        val priv = PKCS8EncodedKeySpec(decodedKey)

//        val fp = ECFieldFp(ecp.getP())
//        val ec = EllipticCurve(fp, ecp.getA(), ecp.getB())
//        val priv = ECPrivateKeySpec(decodedKeyInt, ECParameterSpec())

        val spriv = kf.generatePrivate(priv)
        dsa.initSign(spriv)

        val strByte = data.toByteArray(charset("UTF-8"))
        dsa.update("Bitmaszyna.pl API:\n".toByteArray())
        dsa.update(strByte)

        val realSig = dsa.sign()
        println("Signature: " + BigInteger(1, realSig).toString(16))
        return String(Base64.encode(realSig, Base64.NO_WRAP))
    }
}