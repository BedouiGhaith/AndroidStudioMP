package com.androidproject.app.appcomponents.activities.admin

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import com.androidproject.app.R
import com.androidproject.app.appcomponents.connection.ApiInterface
import com.androidproject.app.appcomponents.models.Product
import com.bumptech.glide.Glide
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream


class AddProductFragment : Fragment() {

    private lateinit var previewImage:ImageView
    private lateinit var name:TextInputEditText
    private lateinit var price:TextInputEditText
    private lateinit var getImage:Button
    private lateinit var add:Button

    private val selectImageFromGalleryResult = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let { Glide.with(requireContext()).load(uri).into(previewImage) }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_add_product, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        previewImage = view.findViewById(R.id.preview_product_admin)
        name = view.findViewById(R.id.prod_name_admin_input)
        price = view.findViewById(R.id.prod_price_admin_input)
        getImage = view.findViewById(R.id.get_image_admin)
        add = view.findViewById(R.id.add_product_admin)

        getImage.setOnClickListener {
            selectImageFromGallery()
        }

        add.setOnClickListener {
            try {
                price.text.toString().toFloat()
                val apiInterface = ApiInterface.create()

                val bitmap = previewImage.drawable.toBitmap()
                val product= Product(id=null, name=name.text.toString(), price = price.text.toString(), image = bitMapToString(bitmap))



                apiInterface.addProducts(product).enqueue(object : Callback<Product>{
                    override fun onResponse(call: Call<Product>, response: Response<Product>) {
                        val result= response.body()
                        if (result != null) {
                            Toast.makeText(requireContext(), "Product Added " + result.id!!, Toast.LENGTH_SHORT).show()
                            previewImage.setImageResource(android.R.color.transparent)
                            name.setText("")
                            price.setText("")
                        }else{
                            Toast.makeText(requireContext(), "Error ", Toast.LENGTH_SHORT).show()
                        }

                    }

                    override fun onFailure(call: Call<Product>, t: Throwable) {
                        Toast.makeText(requireContext(), "Error Server", Toast.LENGTH_SHORT).show()
                    }

                })
            }catch (e: NumberFormatException) {
                    Toast.makeText(requireContext(), "Input Error", Toast.LENGTH_SHORT).show()
                }
            }

        }

    private fun selectImageFromGallery() = selectImageFromGalleryResult.launch("image/*")

    private fun bitMapToString(bitmap: Bitmap): String {
        val baos = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos)
        val b = baos.toByteArray()
        return Base64.encodeToString(b, Base64.DEFAULT)
    }
}
